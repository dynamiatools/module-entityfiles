/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.dynamia.modules.entityfile;

/**
 *
 * @author programador
 */
public class EntityFileException extends RuntimeException{

    public EntityFileException(Throwable thrwbl) {
        super(thrwbl);
    }

    public EntityFileException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public EntityFileException(String string) {
        super(string);
    }

    public EntityFileException() {
    }

}
