package dao.queries;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class InsertContactDetails extends BatchSqlUpdate {
    private static final int BATCH_SIZE = 10;

    public InsertContactDetails(DataSource ds) {
        super(ds, ContactQuery.INSERT_DETAILS);
        declareParameter(new SqlParameter("contact_id", Types.INTEGER));
        declareParameter(new SqlParameter("tel_type", Types.VARCHAR));
        declareParameter(new SqlParameter("tel_number", Types.VARCHAR));
        setGeneratedKeysColumnNames("id");
        setReturnGeneratedKeys(true);
        setBatchSize(10);

    }
}
