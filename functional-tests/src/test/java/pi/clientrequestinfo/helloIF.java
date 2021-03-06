/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package pi.clientrequestinfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.omg.CORBA.*;
import ClientRequestInfo.*;

/**
 * Hello interface for RMI-IIOP version of test
 */
public interface helloIF
    extends Remote
{
  String sayHello () throws RemoteException;
  String saySystemException () throws RemoteException;
  void sayUserException () throws ExampleException, RemoteException;
  void sayOneway () throws RemoteException;
  String sayArguments( String arg1, int arg2, boolean arg3 )
      throws RemoteException;
  void clearInvoked () throws RemoteException;
  boolean wasInvoked () throws RemoteException;
  void resetServant () throws RemoteException;
}

