/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back.controller;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author TOSHIBA
 */
class PostEvent {

    private @Getter @Setter long val;
    private @Getter @Setter String event;
    
    PostEvent(Long val, String event) {
       
    }
    
}
