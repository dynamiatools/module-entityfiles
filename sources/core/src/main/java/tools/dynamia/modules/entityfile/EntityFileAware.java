/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile;

/**
 * Use this interface in entitys to know exactly how many files the entity has attached. The EntityFile module automatically count the files each time you
 * attach a file to entity
 *
 * @author mario
 */
public interface EntityFileAware {

    public Long getFilesCount();

    public void setFilesCount(Long cout);
}
