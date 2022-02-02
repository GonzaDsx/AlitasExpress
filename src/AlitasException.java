/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gonzalo
 */
public class AlitasException extends Exception{
    String e;
    public AlitasException(String e){
        super();
        this.e = e;
    }
    
    public String getMessage(){
        return e;
    }
}
