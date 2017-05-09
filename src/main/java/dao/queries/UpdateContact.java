package dao.queries;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class UpdateContact extends SqlUpdate {

    public UpdateContact(DataSource ds) {
        super(ds, ContactQuery.UPDATE);
        declareParameter(new SqlParameter("first_name", Types.VARCHAR));
        declareParameter(new SqlParameter("second_name", Types.VARCHAR));
        declareParameter(new SqlParameter("date", Types.DATE));
        declareParameter(new SqlParameter("id", Types.INTEGER));
    }
}
