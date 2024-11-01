package by.dudkin.common.entity;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public interface BaseEntity<ID extends Serializable> {

    void setId(ID id);

    ID getId();

}
