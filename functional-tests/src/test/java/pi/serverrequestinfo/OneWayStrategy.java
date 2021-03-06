/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package pi.serverrequestinfo;

import com.sun.corba.ee.impl.interceptors.*;
import org.omg.PortableInterceptor.*;

/**
 * Strategy to test response_expected()
 */
public class OneWayStrategy
    extends InterceptorStrategy
{

    public void receive_request_service_contexts (
        SampleServerRequestInterceptor interceptor, ServerRequestInfo ri)
        throws ForwardRequest
    {
        super.receive_request_service_contexts( interceptor, ri );

        try {
            checkResponseExpected( "rrsc", ri );
        }
        catch( Exception e ) {
            failException( "rrsc", e );
        }
    }

    public void receive_request (
        SampleServerRequestInterceptor interceptor, ServerRequestInfo ri)
    {
        super.receive_request( interceptor, ri );
        
        try {
            checkResponseExpected( "receive_request", ri );
        }
        catch( Exception e ) {
            failException( "receive_request", e );
        }
    }

    public void send_reply (
        SampleServerRequestInterceptor interceptor, ServerRequestInfo ri)
    {
        super.send_reply( interceptor, ri );
        
        try {
            checkResponseExpected( "send_reply", ri );
        }
        catch( Exception e ) {
            failException( "send_reply", e );
        }
    }


    private void checkResponseExpected( String method, ServerRequestInfo ri ) {
        String operationName = ri.operation();
        boolean responseExpected = ri.response_expected();
        boolean validExpected = !operationName.equals( "sayOneway" );

        log( method + "(): Operation " + operationName + 
             ", response expected = " + responseExpected );

        if( responseExpected != validExpected ) {
            fail( "response_expected() invalid for this operation." );
        }
    }

}
