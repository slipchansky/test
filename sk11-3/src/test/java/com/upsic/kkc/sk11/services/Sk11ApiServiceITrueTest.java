package com.upsic.kkc.sk11.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.upsic.kkc.sk11.dto.LuaScriptDto;
import com.upsic.kkc.sk11.dto.Sk11ModelDto;
import com.upsic.kkc.sk11.dto.Sk11NodeDto;
import com.upsic.kkc.sk11.services.annotations.Sk11Itest;

@Sk11Itest
@Disabled // закомментируй это чтобы выполнить.
class Sk11ApiServiceITrueTest {

    private static final String LUA_CATEGORIES = 
            "local objects = snapshot.GetObjects('HisPartition')"
            + " for i=1,#objects do"
            + "   out.AddRecord({"
            + "     uid = objects[i].uid,"
            + "     name = objects[i].name,"
            + "     objectType = objects[i]:ClassName()"
            + "   })"
            + " end ";
    
    @Autowired
    Sk11ApiService sk11ApiService;

    @Test
    void testGetModels() {
        List<Sk11ModelDto> models = sk11ApiService.getModels();
        assertTrue(models.size() > 0);
    }

    @Test
    void testExecuteLuaScript() {
        LuaScriptDto luaScriptDto = LuaScriptDto.builder().luaScript(LUA_CATEGORIES).name("test").build();
        List<Sk11NodeDto> result = sk11ApiService.executeLuaScript(luaScriptDto, Sk11NodeDto.class);
        assertTrue(result.size() > 0);
    }

}
