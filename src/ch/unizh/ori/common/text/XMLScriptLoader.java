/*
 * Created on 09.03.2004
 */
package ch.unizh.ori.common.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;

/**
 * @author pht
 */
public class XMLScriptLoader {
	
	public static List load(){
		List ret = null;
		try {
			Digester digester = new Digester();
			digester.setValidating( false );
			digester.addObjectCreate( "scripts", "class", ArrayList.class );
			digester.addObjectCreate( "scripts/script", "class", DefaultScript.class );
			digester.addSetProperties( "scripts/script" );
			digester.addSetNext("scripts/script", "add");
			
			digester.addObjectCreate( "scripts/script/encoding", "class", ReflectionEncoding.class );
			digester.addSetNext("scripts/script/encoding","addEncoding");
			digester.addSetTop("scripts/script/encoding", "setScript");
			digester.addBeanPropertySetter("scripts/script/encoding/id");
			digester.addBeanPropertySetter("scripts/script/encoding/name");
			digester.addBeanPropertySetter("scripts/script/encoding/description");
			
			digester.addSetProperties( "scripts/script/encoding/method",
					new String[]{ "class", "convert", "create" },
					new String[]{ "className", "convertName", "createName" });

			File input = new File( "etc/lang.xml" );
			ret = (List)digester.parse( input );
		}catch( Exception exc ) {
			exc.printStackTrace();
		}
		return ret;
	}
	
	public static void main(String[] args) {
		List ret = load();
		System.out.println(ret);
	}

}
