/**
 *      Copyright 2006 - Luca Guidi / thedigitalconspiracy.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/****************************************************************************/
/*                             STRING CLASS                                 */
/****************************************************************************/
/**
 * String
 * Extending String class
 *
 * @author 			Luca Guidi
 * @version 			1.0
 * @since 			1.0
 * 
 * date				2006 Oct, 30
 * last revision		2006 Oct, 30
 */
 
    /**
     * Check if two strings are equals
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 26
     * last revision		2006 Oct, 26
     *
     * @param               	sString
     *
     * @return              	true, if are equals
     *                      	false, if not
     */
 /*boolean*/ String.prototype.equals = function(/*String*/ sString){
        return (this == sString) ? true : false;
    }
 
/****************************************************************************/
/*                           EXCEPTION CLASS                                */
/****************************************************************************/
/**
 * Exception
 * Provides an exception handling
 *
 * @author 			Luca Guidi
 * @version 			1.0
 * @since 			1.0
 * 
 * date				2006 Oct, 30
 * last revision		2006 Oct, 30
 */
 var Exception = {
    /*static String*/ INVALID_ARGUMENT : 'Invalid argument',
    
    /**
     * Provides an exception handling
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 30
     * last revision		2006 Oct, 30
     *
     * @param			eError, a JavaScript Error object
     *				that we want to throw.
     *
     * @return			A string with useful error informations.
     */    
    /*static String*/ throwEx : function(/*Error*/ eError){
        if( !(eError instanceof Error) )
            throw new Error('Invalid Exception Handling');
        
        return 'Exception: '+ eError.message +' @ ' + eError.fileName
            +':'+(eError.lineNumber - 1);
    }
 }
/****************************************************************************/
/*                            HASHMAP CLASS                                 */
/****************************************************************************/

/**
 * HashMap
 * 
 * @author 			Luca Guidi
 * @version 			1.0
 * 
 * date				2006 Oct, 25
 * last revision		2006 Oct, 30
 */
 HashMap = Class.create();
 
 HashMap.prototype = {
    
    /*struct*/  oStruct     : null,
    /*int*/     sSize       : 0,    
    
    /**
     * Constructor.
     * Required by Prototype.
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 30
     * last revision		2006 Oct, 30
     */
    /*HashMap*/ initialize : function(){
        this.oStruct = {}
        return;
    },
 
    /**
     * Clear the hashmap contents
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 30
     * last revision		2006 Oct, 30
     */
    /*void*/ clear : function(){
        this.oStruct = {}
        this.sSize = 0;
        return;
    },
 
    /**
     * Check if contains specified key
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 25
     * last revision		2006 Oct, 25
     *
     * @param       	        sKey
     *
     * @return      	        true, if hashmap contains key
     *              	        false, if not
     */
    /*boolean*/ containsKey : function(/*String*/ sKey){
        for(var x in this.oStruct){
            if(x == sKey && this.oStruct[sKey] != undefined)
                return true;
        }
        
        return false;
    },
    
    /**
     * Check if contains an object with specified value
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 25
     * last revision		2006 Oct, 30
     *
     * @param               	sValue
     *
     * @return              	true, if map contains specified value
     *                      	false, if not
     */
    /*boolean*/ containsValue : function(/*String*/ sValue){
        for(var x in this.oStruct){
            if( this.oStruct[x] != undefined
		&& (this.oStruct[x].toString()).equals(sValue.valueOf()) )
                return true;
        }
        
        return false;
    },
    
    /**
     * Returns the object with specified key
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 24
     * last revision		2006 Oct, 24
     *
     * @param               	sKey
     *
     * @return              	The object with specified key.
     */
    /*Object*/ get : function(/*String*/ sKey){
        if(!this.containsKey(sKey))
            return null;
        return this.oStruct[sKey];
    },
    
    /**
     * Check if hashmap is empty
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 24
     * last revision		2006 Oct, 24
     *
     * @return              	true, if map is empty
     *                      	false, if not
     */
    /*Object*/ isEmpty : function(){
        return (this.sSize <= 0) ? true : false;
    },
   
    /**
     * Set into the hashmap an object with specified key.
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 24
     * last revision		2006 Oct, 30
     *
     * @param               	sKey
     * @param               	oObj
     *
     * @return              	Previous value associated to the key
     */
    /*Object*/ put : function(/*String*/ sKey, /*Object*/ oObj){
        if( !(oObj instanceof Object) )
            throw Exception.throwEx(new Error(Exception.INVALID_ARGUMENT));
        
        oOldObj = null;
        //If key's not in Map, we need to increment size
        if( this.containsKey(sKey) )
            oOldObj = this.get(sKey);
        else
            this.sSize++;
   
        this.oStruct[sKey] = oObj;

        return oOldObj;
    },
        
    /**
     * Remove the object at specified key.
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 24
     * last revision		2006 Oct, 30
     *
     * @param               	sKey
     *
     * @return              	The removed object
     */
    /*Object*/ remove : function(/*String*/ sKey){
        if(!this.containsKey(sKey))
            return null;
        oldObj = this.oStruct[sKey];
        this.oStruct[sKey] = undefined;
        this.sSize--;
        
        return oldObj;
    },
    
    /**
     * Return hashmap size
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 30
     * last revision		2006 Oct, 30
     *
     * @return              	map size
     */
    /*int*/ size : function(){
        return this.sSize;
    },
    
    /**
     * Returns selializated internal contents.
     * "[k0 : objK0, k1 : objK1, k2 : objK2, ..., kn : objKn]"
     *
     * @author 			Luca Guidi
     * @version 		1.0
     * @since 			1.0
     * 
     * date			2006 Oct, 30
     * last revision		2006 Oct, 30
     *
     * @return              	Serializated contents.
     */
    /*String*/ toString : function(){
        var str = '[';
        for(var x in this.oStruct)
            str += x+' : '+this.oStruct[x]+','
        str += ']'
        return str;
    }
}
