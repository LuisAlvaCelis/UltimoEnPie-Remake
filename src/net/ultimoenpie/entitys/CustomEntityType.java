package net.ultimoenpie.entitys;

import java.lang.reflect.Method;
import java.util.Map;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_16_R1.DataConverterRegistry;
import net.minecraft.server.v1_16_R1.DataConverterTypes;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.SharedConstants;

public class CustomEntityType{
	
 
    @SuppressWarnings( "unchecked")
	public EntityTypes register(String name,String extend_from,Class<?> entityTypes){
    	Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>)DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY_TREE).types();
        dataTypes.put("minecraft:"+name, dataTypes.get("minecraft:"+extend_from));
        
        Method a;
        Object ret=null;
        try {
        	a = EntityTypes.class.getDeclaredMethod("a", Class.class,String.class);
            a.setAccessible(true);
            ret=a.invoke(null,entityTypes,name);
		} catch (Exception e) {
		}
        return (EntityTypes)ret;
    }
 
}