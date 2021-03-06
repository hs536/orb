/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1997-1999 IBM Corp. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.tools.corba.ee.idl;

// NOTES:

import java.io.PrintWriter;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 * This is the base class for all symbol table entries.
 * @see AttributeEntry
 * @see com.sun.tools.corba.ee.idl.ConstEntry
 * @see com.sun.tools.corba.ee.idl.EnumEntry
 * @see com.sun.tools.corba.ee.idl.ExceptionEntry
 * @see com.sun.tools.corba.ee.idl.IncludeEntry
 * @see com.sun.tools.corba.ee.idl.InterfaceEntry
 * @see com.sun.tools.corba.ee.idl.MethodEntry
 * @see com.sun.tools.corba.ee.idl.ModuleEntry
 * @see com.sun.tools.corba.ee.idl.ParameterEntry
 * @see com.sun.tools.corba.ee.idl.PragmaEntry
 * @see com.sun.tools.corba.ee.idl.PrimitiveEntry
 * @see com.sun.tools.corba.ee.idl.SequenceEntry
 * @see com.sun.tools.corba.ee.idl.StructEntry
 * @see com.sun.tools.corba.ee.idl.TypedefEntry
 * @see com.sun.tools.corba.ee.idl.UnionEntry
 **/
public class SymtabEntry
{
  public SymtabEntry ()
  {
    initDynamicVars ();
  } // ctor

  SymtabEntry (SymtabEntry that, com.sun.tools.corba.ee.idl.IDLID clone)
  {
    _module     = that._module;
    _name       = that._name;
    _type       = that._type;
    _typeName   = that._typeName;
    _sourceFile = that._sourceFile;
    _info       = that._info;
    _repID      = (com.sun.tools.corba.ee.idl.RepositoryID)clone.clone ();
    ((com.sun.tools.corba.ee.idl.IDLID)_repID).appendToName (_name);
    if (that instanceof com.sun.tools.corba.ee.idl.InterfaceEntry || that instanceof com.sun.tools.corba.ee.idl.ModuleEntry || that instanceof com.sun.tools.corba.ee.idl.StructEntry || that instanceof com.sun.tools.corba.ee.idl.UnionEntry || (that instanceof com.sun.tools.corba.ee.idl.SequenceEntry && this instanceof com.sun.tools.corba.ee.idl.SequenceEntry))
      _container = that;
    else
      _container = that._container;
    initDynamicVars ();
        _comment = that._comment;       // <21jul1997daz>
  } // ctor

  /** This is a shallow copy constructor
   * @param that SymtabEntry to copy
   */
  SymtabEntry (SymtabEntry that)
  {
    _module     = that._module;
    _name       = that._name;
    _type       = that._type;
    _typeName   = that._typeName;
    _sourceFile = that._sourceFile;
    _info       = that._info;
    _repID      = (com.sun.tools.corba.ee.idl.RepositoryID)that._repID.clone ();
    _container  = that._container;

    if (_type instanceof com.sun.tools.corba.ee.idl.ForwardEntry)
      ((com.sun.tools.corba.ee.idl.ForwardEntry)_type).types.addElement (this);

    initDynamicVars ();
        // <21JUL1997>
        _comment = that._comment;
  } // ctor

  void initDynamicVars ()
  {
    _dynamicVars = new Vector (maxKey + 1);
    for (int i = 0; i <= maxKey; ++i)
      _dynamicVars.addElement (null);
  } // initDynamicVars

  /** This is a shallow copy clone */
  @Override
  public Object clone ()
  {
    return new SymtabEntry (this);
  } // clone

  /** @return the concatenation of the module and the name, delimited by '/'. */
  public final String fullName ()
  {
    return _module.equals ("") ? _name : _module + '/' + _name;
  } // fullName

  /** Get the name of this entry's module.  If there are modules within
      modules, each module name is separated by '/'.
      @return this entry's module name. */
  public String module ()
  {
    return _module;
  } // module

  /** Set the module for this entry.
      @param newName the new name of the module. */
  public void module (String newName)
  {
    if (newName == null)
      _module = "";
    else
      _module = newName;
  } // module

  /** @return the name of this entry. */
  public String name ()
  {
    return _name;
  } // name

  /** Set the name.
      @param newName the new name. */
  public void name (String newName)
  {
    if (newName == null)
      _name = "";
    else
      _name = newName;

    // Update the RepositoryID
    if (_repID instanceof com.sun.tools.corba.ee.idl.IDLID)
      ((com.sun.tools.corba.ee.idl.IDLID)_repID).replaceName (newName);
  } // name

  /** @return the type name of this entry. */
  public String typeName ()
  {
    return _typeName;
  } // typeName

  protected void typeName (String typeName)
  {
    _typeName = typeName;
  } // typeName

  /** @return the type entry of this entry */
  public SymtabEntry type ()
  {
    return _type;
  } // type

  public void type (SymtabEntry newType)
  {
    if (newType == null)
      typeName ("");
    else
      typeName (newType.fullName ());
    _type = newType;

    if (_type instanceof com.sun.tools.corba.ee.idl.ForwardEntry)
      ((com.sun.tools.corba.ee.idl.ForwardEntry)_type).types.addElement (this);
  } // type

  /** The file name in which this entry was defined. 
   * @return {@link IncludeEntry} of fileName
   */
  public com.sun.tools.corba.ee.idl.IncludeEntry sourceFile ()
  {
    return _sourceFile;
  } // sourceFile

  /** The file name in which this entry was defined.
   * @param file {@link IncludeEntry} of the file 
   */
  public void sourceFile (com.sun.tools.corba.ee.idl.IncludeEntry file)
  {
    _sourceFile = file;
  } // sourceFile

  /** This must be either an InterfaceEntry or a ModuleEntry.
   * It can be nothing else.
   * @return container of the entry
   */
  public SymtabEntry container ()
  {
    return _container;
  } // container

  /** This must be either an InterfaceEntry or a ModuleEntry.
    *  It can be nothing else. 
    * @param newContainer {@link InterfaceEntry} or {@link ModuleEntry}
    */
  public void container (SymtabEntry newContainer)
  {
    if (newContainer instanceof com.sun.tools.corba.ee.idl.InterfaceEntry || newContainer instanceof com.sun.tools.corba.ee.idl.ModuleEntry)
      _container = newContainer;
  } // container

  /** @return the repository ID for this entry. */
  public com.sun.tools.corba.ee.idl.RepositoryID repositoryID ()
  {
    return _repID;
  } // repositoryID

  /** Set the repository ID for this entry.
      @param id the new repository ID. */
  public void repositoryID (com.sun.tools.corba.ee.idl.RepositoryID id)
  {
    _repID = id;
  } // repositoryID

  /** Should this type be emitted? 
   * @return true if this type is emitted and referencable
   */
  public boolean emit ()
  {
    return _emit && _isReferencable ;
  } // emit

  public void emit (boolean emit)
  {
    _emit = emit;
  } // emit

  /* <21jul1997daz> Accessors for comment */

  public com.sun.tools.corba.ee.idl.Comment comment()
  {
    return _comment;
  }

  public void comment( com.sun.tools.corba.ee.idl.Comment comment )
  {
    _comment = comment;
  }

  public boolean isReferencable()
  {
    return _isReferencable ;
  }

  public void isReferencable( boolean value ) 
  {
    _isReferencable = value ;
  }

  static Stack<Boolean> includeStack = new Stack<>();

  static void enteringInclude ()
  {
    includeStack.push (setEmit);
    setEmit = false;
  } // enteringInclude

  static void exitingInclude ()
  {
    setEmit = includeStack.pop();
  } // exitingInclude

  /** Other variables besides the default ones can be dynamically placed
    * into SymTabEntry (and therefore on all symbol table entries) by
    * extenders.  Before such a variable can exist, its key must be
    * obtained by calling getVariableKey.
    * @return the key for placing variables into the entry
    */
  public static int getVariableKey ()
  {
    return ++maxKey;
  } // dynamicVariable

  /** Other variables besides the default ones can be dynamically placed
    * into SymTabEntry (and therefore on all symbol table entries) by
    * extenders.  This method assigns the value to the variable of the
    * given key.  A valid key must be obtained by calling the method
    * getVariableKey.  If the key is invalid, NoSuchFieldException is
    * thrown.
    * @param key obtained by {@link #getVariableKey()}
    * @param value variable to assign to key
    * @throws NoSuchFieldException if key is invalid
    */
  public void dynamicVariable (int key, Object value) throws NoSuchFieldException
  {
    if (key > maxKey)
      throw new NoSuchFieldException (Integer.toString (key));
    else
    {
      if (key >= _dynamicVars.size ())
        growVars ();
      _dynamicVars.setElementAt (value, key);
    }
  } // dynamicVariable

  /** Other variables besides the default ones can be dynamically placed
      into SymTabEntry (and therefore on all symbol table entries) by
      extenders.  This method gets the value of the variable of the
      given key.  A valid key must be obtained by calling the method
      getVariableKey.  If the key is invalid, {@link NoSuchFieldException} is
      thrown. 
    * @param key obtained by calling {@link #getVariableKey()}
    * @return the variable for the key
    * @throws NoSuchFieldException if key is invalid
    */
  public Object dynamicVariable(int key) throws NoSuchFieldException
  {
    if (key > maxKey)
      throw new NoSuchFieldException (Integer.toString (key));
    else
    {
      if (key >= _dynamicVars.size ())
        growVars ();
      return _dynamicVars.elementAt (key);
    }
  } // dynamicVariable

  void growVars ()
  {
    int diff = maxKey - _dynamicVars.size () + 1;
    for (int i = 0; i < diff; ++i)
      _dynamicVars.addElement (null);
  } // growVars

  /** Invoke a generator.  A call to this method is only meaningful
      for subclasses of SymtabEntry.  If called on this class, it
      is a no-op.
      @param symbolTable the symbol table is a hash table whose key is
       a fully qualified type name and whose value is a SymtabEntry or
       a subclass of SymtabEntry.
      @param stream the stream to which the generator should sent its output. */
  public void generate (Hashtable symbolTable, PrintWriter stream)
  {
  } // generate

  /** Access a generator.  A call to this method is only meaningful
      for subclasses of SymtabEntry.  If called on this class, it
      is a no-op.
      @return an object which implements the Generator interface. */
  public com.sun.tools.corba.ee.idl.Generator generator ()
  {
    return null;
  } // generator

          static boolean setEmit   = true;
          static int   maxKey      = -1;

  private SymtabEntry  _container  = null;
  private String       _module     = "";
  private String       _name       = "";
  private String       _typeName   = "";
  private SymtabEntry  _type       = null;
  private com.sun.tools.corba.ee.idl.IncludeEntry _sourceFile = null;
  private Object       _info       = null;
  private com.sun.tools.corba.ee.idl.RepositoryID _repID      = new com.sun.tools.corba.ee.idl.IDLID("", "", "1.0");
  private boolean      _emit       = setEmit;
  private com.sun.tools.corba.ee.idl.Comment _comment    = null;
  private Vector       _dynamicVars;
  private boolean      _isReferencable = true ;
} // class SymtabEntry

/*=======================================================================================
  DATE<AUTHOR>   ACTION
  ---------------------------------------------------------------------------------------
  21jul1997<daz> Added _comment data member to afford transferring comments from source
                 file to target; added acessor methods for comment.
  =======================================================================================*/
