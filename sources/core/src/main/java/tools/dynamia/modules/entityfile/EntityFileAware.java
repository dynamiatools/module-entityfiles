
package tools.dynamia.modules.entityfile;

/**
 * Use this interface in entitys to know exactly how many files the entity has attached. The EntityFile module automatically count the files each time you
 * attach a file to entity
 *
 * @author Mario Serrano Leones
 */
public interface EntityFileAware {

    Long getFilesCount();

    void setFilesCount(Long cout);
}
