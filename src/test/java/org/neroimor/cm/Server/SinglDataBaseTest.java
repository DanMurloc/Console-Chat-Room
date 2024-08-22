package org.neroimor.cm.Server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinglDataBaseTest {

    @Test
    void addAndGetUser() {
        boolean res = SinglDataBase.addAndGetUser("Даня");
        assertFalse(res);
        boolean res2 = SinglDataBase.addAndGetUser("Дима");
        assertTrue(res2);
    }
}