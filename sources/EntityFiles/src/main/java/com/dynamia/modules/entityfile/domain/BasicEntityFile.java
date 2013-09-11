/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.modules.entityfile.domain.enums.EntityFileState;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.tools.domain.BaseEntity;
import com.dynamia.tools.io.IOUtils;

/**
 *
 * @author djinn
 */
@Entity
@Table(name = "mod_entity_files")
    
public class BasicEntityFile extends BaseEntity {

    @OneToMany(mappedBy = "parentDirectory")
    private List<BasicEntityFile> children;
    @NotNull(message = "Ingrese clase dependencia valida")
    private String targetEntity;
    private Long targetEntityId;
    @NotNull(message = "Ingrese un nombre valido")
    private String name;
    @NotNull(message = "Ingrese tipo de archivo")
    private EntityFileType type;
    private String extension = "dir";
    private String contentType;
    @ManyToOne
    private BasicEntityFile parentDirectory;
    private boolean shared;
    private Long size = 0l;
    private String description;
    private EntityFileState state;
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripcion) {
        this.description = descripcion;
    }

    public BasicEntityFile getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(BasicEntityFile parentDirectory) {
        if (parentDirectory != null && parentDirectory.getType() == EntityFileType.DIRECTORY) {
            this.parentDirectory = parentDirectory;
        }
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String ext) {
        this.extension = ext;
    }

    public List<BasicEntityFile> getChildren() {
        return children;
    }

    public void setChildren(List<BasicEntityFile> filesHijos) {
        this.children = filesHijos;
    }

    public Long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(Long idDependencia) {
        this.targetEntityId = idDependencia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long sizeFile) {
        this.size = sizeFile;
    }

    public EntityFileType getType() {
        return type;
    }

    public void setType(EntityFileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getSizeAsString() {
        if (size != null) {
            return IOUtils.formatFileSize(size.longValue());
        } else {
            return null;
        }
    }

    public void setState(EntityFileState state) {
        this.state = state;
    }

    public EntityFileState getState() {
        return state;
    }

}
