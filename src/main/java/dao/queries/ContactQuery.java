package dao.queries;

public final class ContactQuery {
    public static final String SELECT_ALL = "SELECT * FROM contact";
    public static final String SELECT_BY_FIRST_NAME = "SELECT * FROM contact WHERE first_name=:name";
    public static final String SELECT_FIRST_NAME_BY_ID = "SELECT contact.first_name FROM contact WHERE id=:id";
    public static final String SELECT_LAST_NAME_BY_ID = "SELECT contact.last_name FROMM contact WHERE id=:id";
    public static final String SELECT_ALL_WITH_DETAILS =
            "SELECT c.id, c.first_name, c.second_name, c.birth_date, " +
                    "d.id as tel_id, d.contact_id, d.tel_type, d.tel_number " +
                    "FROM contact c " +
                    "LEFT JOIN contact_tel_detail d ON c.id = d.contact_id";

    public static final String INSERT_ALL = "INSERT INTO contact(first_name, second_name, birth_date) \n" +
            "  VALUES(:first_name, :second_name, :birth_date)";
    public static final String INSERT_DETAILS = "INSERT INTO contact_tel_detail(contact_id, tel_type, tel_number)" +
            " VALUES (:contact_id, :tel_type, :tel_number)";

    public static final String UPDATE = "UPDATE contact " +
            "SET first_name=:first_name, second_name=:second_name, birth_date=:date " +
            "WHERE id=:id";
    public static final String UPDATE_DETAILS = "UPDATE contact_tel_detail " +
            "SET contact_id=:contact_id, tel_number=:tel_number, tel_type=:tel_type " +
            "WHERE id=:id";
}
