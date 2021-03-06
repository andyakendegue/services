/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.unittests.address;

import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sola.services.common.EntityAction;
import org.sola.services.ejb.address.businesslogic.AddressEJB;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;
import org.sola.services.common.test.AbstractEJBTest;

/**
 *
 * @author soladev
 */
public class AddressEJBIT extends AbstractEJBTest {

    private static String ADDR_MODULE_NAME = "sola-address-1_0-SNAPSHOT";

    public AddressEJBIT() {
        super();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Performs CRUD tests for the Address EJB
     */
    @Test
    public void testSaveAddress() throws Exception {

        System.out.println("testSaveAddress...");
        Address address = new Address();
        address.setDescription("My first Address");
        AddressEJBLocal instance = (AddressEJBLocal) getEJBInstance(ADDR_MODULE_NAME,
                AddressEJB.class.getSimpleName());

        // Create the Address
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            System.out.println(">>> Create Address");
            Address result = instance.saveAddress(address);
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(address.getDescription(), result.getDescription());
            assertEquals(1, result.getRowVersion());
            assertNull(result.getEntityAction());
            assertNull(result.getExtAddressId());
            System.out.println("Id=" + result.getId());
            String addressId = result.getId();

            // Retrieve Address
            System.out.println(">>> Retrieve Address");
            Address result5 = instance.getAddress(addressId);
            assertNotNull(result5);
            assertEquals(addressId, result5.getId());
            assertEquals(1, result5.getRowVersion());

            // Update the Address
            System.out.println(">>> Update Address");
            result.setDescription("Updated Address");
            Address result2 = instance.saveAddress(result);
            assertNotNull(result2);
            assertEquals(result.getId(), result2.getId());
            assertEquals(result.getDescription(), result2.getDescription());
            assertEquals(2, result2.getRowVersion());
            assertNull(result2.getEntityAction());
            assertNull(result2.getExtAddressId());


            // Disassociate the Address
            System.out.println(">>> Disassociate Address");
            result2.setEntityAction(EntityAction.DISASSOCIATE);
            Address result3 = instance.saveAddress(result2);
            assertNull(result3);


            // Delete the Address
            System.out.println(">>> Delete Address");
            result2.setEntityAction(EntityAction.DELETE);
            result3 = instance.saveAddress(result2);
            assertNull(result3);

            System.out.println(">>> Retrieve Deleted Address");
            Address result4 = instance.getAddress(addressId);
            assertNull(result4);
            tx.commit();
        } finally {
            if (tx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                tx.rollback();
                System.out.println("Failed Transction!");
            }
        }

    }
}
