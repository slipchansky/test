package com.upsic.kkc.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.upsic.kkc.annotations.Sk11Itest;
import com.upsic.kkc.dto.sk11api.LuaScriptDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.dto.sk11api.Sk11NodeDto;

@Sk11Itest
class Sk11ApiServiceITest {
    
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

    void testExecuteLuaScript() {
          List<Sk11ModelDto> models = sk11ApiService.getModels();
          LuaScriptDto luaScriptDto = LuaScriptDto.builder().luaScript(LUA_CATEGORIES).name("test").build();
          List<Sk11NodeDto> result = sk11ApiService.executeLuaScript(models.get(0).getModelUid().toString(), luaScriptDto, Sk11NodeDto.class);
    }

}
