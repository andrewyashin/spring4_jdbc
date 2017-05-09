package dao;

import dao.entities.Contact;
import dao.entities.ContactTelDetail;
import dao.interfaces.ContactDAO;
import dao.queries.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("contactDAO")
public class AnnotationContactDao implements ContactDAO {
    private final static Log LOG = LogFactory.getLog(AnnotationContactDao.class);
    private DataSource dataSource;
    private NamedParameterJdbcTemplate template;

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @PostConstruct
    public void init(){
        if(dataSource == null){
            throw new BeanCreationException("DataSource is null");
        } else {
            LOG.info("DataSource ready");
        }
    }

    @Override
    public List<Contact> findAll() {
        return new SelectAllContacts(dataSource).execute();
    }

    @Override
    public List<Contact> findAllWithDetails() {
        return template.query(ContactQuery.SELECT_ALL_WITH_DETAILS,
                new ContactWithDetailsMapper());
    }

    @Override
    public List<Contact> findByFirstName(String firstName) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", firstName);

        return new SelectContactsByFirstName(dataSource)
                .executeByNamedParam(params);
    }

    @Override
    public String findFirstNameById(Long id) {
        return "";
    }

    @Override
    public String findLastNameById(Long id) {
        return null;
    }

    @Override
    public void insert(Contact contact) {
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", contact.getFirst_name());
        params.put("second_name", contact.getSecond_name());
        params.put("birth_date", contact.getBirth_date());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        new InsertContact(dataSource).updateByNamedParam(params, keyHolder);

        contact.setId(keyHolder.getKey().longValue());

        LOG.info("Contact put with ID - " + contact.getId());
    }

    @Override
    public void insertWithDetails(Contact contact) {
        InsertContactDetails insertContactDetails = new InsertContactDetails(dataSource);
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", contact.getFirst_name());
        params.put("second_name", contact.getSecond_name());
        params.put("birth_date", contact.getBirth_date());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        new InsertContact(dataSource).updateByNamedParam(params, keyHolder);

        contact.setId(keyHolder.getKey().longValue());

        LOG.info("Contact put with ID - " + contact.getId());

        List<ContactTelDetail> details = contact.getDetails();

        if(details != null){
            for(ContactTelDetail detail : details){
                params = new HashMap<>();
                params.put("contact_id", contact.getId());
                params.put("tel_number", detail.getTel_number());
                params.put("tel_type", detail.getTel_type());
                insertContactDetails.updateByNamedParam(params, keyHolder);
                detail.setId(keyHolder.getKey().longValue());

                LOG.info("Put Detail with ID - " + detail.getId());

            }
        }

        insertContactDetails.flush();

    }

    @Override
    public void update(Contact contact) {
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", contact.getFirst_name());
        params.put("second_name", contact.getSecond_name());
        params.put("date", contact.getBirth_date());
        params.put("id", contact.getId());
        new UpdateContact(dataSource).updateByNamedParam(params);
        LOG.info("Contact update with ID - " + contact.getId());
    }

    @Override
    public void delete(Long id) {

    }

    private static class ContactWithDetailsMapper implements ResultSetExtractor<List<Contact>> {
        @Override
        public List<Contact> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Long, Contact> map = new HashMap<>();
            Contact contact = null;

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                contact = map.get(id);

                if(contact == null){
                    contact = new Contact();
                    contact.setId(id);
                    contact.setFirst_name(resultSet.getString("first_name"));
                    contact.setSecond_name(resultSet.getString("second_name"));
                    contact.setBirth_date(resultSet.getDate("birth_date"));
                    contact.setDetails(new ArrayList<>());
                    map.put(id, contact);
                }

                Long contact_tel_detail = resultSet.getLong("tel_id");

                if (contact_tel_detail > 0){
                    ContactTelDetail detail = new ContactTelDetail();
                    detail.setId(resultSet.getLong("tel_id"));
                    detail.setContact_id(resultSet.getLong("contact_id"));
                    detail.setTel_number(resultSet.getString("tel_number"));
                    detail.setTel_type(resultSet.getString("tel_type"));
                    contact.getDetails().add(detail);
                }

            }

            return new ArrayList<>(map.values());
        }
    }
}
