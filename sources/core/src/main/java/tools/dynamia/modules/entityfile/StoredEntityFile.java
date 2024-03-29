/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.dynamia.modules.entityfile;

import java.io.File;
import java.io.Serializable;

import tools.dynamia.modules.entityfile.domain.EntityFile;

public abstract class StoredEntityFile implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 421213041955145817L;
	private EntityFile entityFile;
	private String url;
	private File realFile;

	public StoredEntityFile(EntityFile entityFile, String url, File realFile) {
		super();
		this.entityFile = entityFile;
		this.url = url;
		this.realFile = realFile;
	}

	public EntityFile getEntityFile() {
		return entityFile;
	}

	public String getUrl() {
		return url;
	}

	public String getThumbnailUrl() {
		return getThumbnailUrl(200, 200);
	}

	public abstract String getThumbnailUrl(int width, int height);

	public File getRealFile() {
		return realFile;
	}

}
