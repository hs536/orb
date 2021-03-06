/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1998-1999 IBM Corp. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package javax.rmi.CORBA.serialization;

/**
* Date.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from java/util/Date.idl
* 01 June 1999 20:22:16 o'clock GMT+00:00
*/

public abstract class Date implements javax.rmi.CORBA.serialization.Cloneable, org.omg.CORBA.portable.CustomValue 
{
  private static String[] _truncatable_ids = {
    DateHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public abstract long UTC (int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

  public abstract boolean after (Date arg0);

  public abstract boolean before (Date arg0);

  public abstract boolean _equals (org.omg.CORBA.Any arg0);

  public abstract int date ();

  public abstract void date (int newDate);

  public abstract int day ();

  public abstract int hours ();

  public abstract void hours (int newHours);

  public abstract int minutes ();

  public abstract void minutes (int newMinutes);

  public abstract int month ();

  public abstract void month (int newMonth);

  public abstract int seconds ();

  public abstract void seconds (int newSeconds);

  public abstract long time ();

  public abstract void time (long newTime);

  public abstract int timezoneOffset ();

  public abstract int year ();

  public abstract void year (int newYear);

  public abstract int _hashCode ();

  public abstract long parse (String arg0);

  public abstract String toGMTString ();

  public abstract String toLocaleString ();

  public abstract String _toString ();

} // class Date
