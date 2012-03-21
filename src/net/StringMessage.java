/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Hans
 */
@Serializable
public class StringMessage extends AbstractMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public StringMessage() {
    }

    public StringMessage(String message) {
        this.message = message;
    }
}
