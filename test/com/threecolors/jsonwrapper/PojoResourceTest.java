package com.threecolors.jsonwrapper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.threecolors.jsonwrapper.PojoResource;

public class PojoResourceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRepresent() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testConverCamelCase() {
        String camel = PojoResource.convertCamelCase("TB_CATE");
        assertEquals("tbCate", camel);
        camel = PojoResource.convertCamelCase("TbCate");
        assertEquals("tbcate", camel);
        camel = PojoResource.convertCamelCase("Tb_Cate");
        assertEquals("tbCate", camel);
    }

    @Test
    public void testGeneratePojo() {
        String jsonStr = "{\"dataTableNm\": \"TB_ANSWER_DETAIL\",\"rows\":[{\"_CHANGE_TYPE\":\"I\",\"FACILITY_CD\":\"10107\",\"VISIT_NO\":\"121213135\",\"TASK_NO\":\"48\",\"TASK_NM\":\"Lift\",\"RCS_HL_YN\":\"N\",\"SUB_TASK_SEQ_NO\":\"1\",\"SUB_TASK_NM\":\"Sit-to-Stand\",\"RCSD_HL_YN\":\"N\"}]}";
        try {
            String pojo = PojoResource.generatePojo(jsonStr);
            assertFalse(pojo.isEmpty());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
