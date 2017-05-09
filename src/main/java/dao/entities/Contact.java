package dao.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by andrew_yashin on 5/6/17.
 */
public class Contact implements Serializable {
    private Long id;
    private String first_name;
    private String second_name;
    private Date birth_date;
    private List<ContactTelDetail> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public List<ContactTelDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ContactTelDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", second_name='" + second_name + '\'' +
                ", birth_date=" + birth_date +
                ", details=" + details +
                '}';
    }
}
