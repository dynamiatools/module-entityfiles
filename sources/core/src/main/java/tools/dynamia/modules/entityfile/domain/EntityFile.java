/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.domain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.domain.BaseEntity;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.IOUtils;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.domain.enums.EntityFileState;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.modules.saas.AccountAware;
import tools.dynamia.modules.saas.domain.Account;

@Entity
@Table(name = "mod_entity_files")
public class EntityFile extends BaseEntity implements AccountAware {

	@OneToMany(mappedBy = "parent")
	private List<EntityFile> children;
	@NotNull(message = "Ingrese clase dependencia valida")
	private String targetEntity;
	private Long targetEntityId;
	private String targetEntitySId;
	@NotNull(message = "Ingrese un nombre valido")
	private String name;
	@NotNull(message = "Ingrese tipo de archivo")
	private EntityFileType type;
	private String extension = "dir";
	private String contentType;
	@ManyToOne
	private EntityFile parent;
	private boolean shared;
	@Column(name = "fileSize")
	private Long size = 0l;
	@Column(length = 1000)
	private String description;
	@Column(name = "fileState")
	@Enumerated(EnumType.ORDINAL)
	private EntityFileState state;

	@OneToOne
	private Account account;

	public String getTargetEntitySId() {
		return targetEntitySId;
	}

	public void setTargetEntitySId(String targetEntitySId) {
		this.targetEntitySId = targetEntitySId;
	}

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

	public EntityFile getParent() {
		return parent;
	}

	public void setParent(EntityFile parentDirectory) {
		if (parentDirectory != null && parentDirectory.getType() == EntityFileType.DIRECTORY) {
			this.parent = parentDirectory;
		}
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String ext) {
		this.extension = ext;
	}

	public List<EntityFile> getChildren() {
		return children;
	}

	public void setChildren(List<EntityFile> filesHijos) {
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public File getRealFile() {
		EntityFileService service = Containers.get().findObject(EntityFileService.class);
		if (service == null) {
			throw new NullPointerException("No EntityService war found to create Real File");
		}
		return service.getRealFile(this);
	}

	public File getThumbnail(int height, int width) {
		try {
			File realFile = getRealFile();
			File thumbFile = new File(realFile.getParentFile().getAbsolutePath() + "/thumbnails/" + height + "x" + width + "/", getId()
					.toString());
			if (!thumbFile.exists()) {
				thumbFile.getParentFile().mkdirs();
				thumbFile.createNewFile();
				if (extension.equals("png")) {
					ImageUtil.resizeImage(realFile, thumbFile, "png", width, height);
				} else {
					ImageUtil.resizeJPEGImage(realFile, thumbFile, width, height);
				}
			}

			return thumbFile;

		} catch (IOException e) {
			throw new EntityFileException("Error creating EntityFile thumbnails " + getName() + ". ID: " + getId(), e);
		}
	}

}
