/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.javascript.model.implementations.declaration;

import com.google.common.collect.Iterators;
import com.sonar.sslr.api.AstNode;
import org.apache.commons.collections.ListUtils;
import org.sonar.javascript.model.implementations.JavaScriptTree;
import org.sonar.javascript.model.implementations.SeparatedList;
import org.sonar.javascript.model.implementations.lexical.InternalSyntaxToken;
import org.sonar.javascript.model.interfaces.Tree;
import org.sonar.javascript.model.interfaces.declaration.BindingElementTree;
import org.sonar.javascript.model.interfaces.declaration.ParameterListTree;
import org.sonar.javascript.model.interfaces.expression.ExpressionTree;
import org.sonar.javascript.model.interfaces.lexical.SyntaxToken;

import java.util.Iterator;

public class ParameterListTreeImpl extends JavaScriptTree implements ParameterListTree {

  private InternalSyntaxToken openParenthesis;
  private final SeparatedList<Tree> parameters;
  private InternalSyntaxToken closeParenthesis;
  private final Kind kind;

  public ParameterListTreeImpl(Kind kind, SeparatedList<Tree> parameters) {
    super(kind);
    this.kind = kind;
    this.parameters = parameters;

    for (AstNode child : parameters.getChildren()) {
      addChild(child);
    }
    parameters.clearChildren();
  }

  public ParameterListTreeImpl(Kind kind, InternalSyntaxToken openParenthesis, InternalSyntaxToken closeParenthesis) {
    super(kind);
    this.kind = kind;
    this.openParenthesis = openParenthesis;
    this.parameters = new SeparatedList<Tree>(ListUtils.EMPTY_LIST, ListUtils.EMPTY_LIST);
    this.closeParenthesis = closeParenthesis;

    prependChildren(openParenthesis);
    addChild(closeParenthesis);
  }

  public ParameterListTreeImpl complete(InternalSyntaxToken openParenthesis, InternalSyntaxToken closeParenthesis) {
    this.openParenthesis = openParenthesis;
    this.closeParenthesis = closeParenthesis;

    prependChildren(openParenthesis);
    addChild(closeParenthesis);
    return this;
  }

  @Override
  public SyntaxToken openParenthesis() {
    return openParenthesis;
  }

  @Override
  public SeparatedList<Tree> parameters() {
    return parameters;
  }

  @Override
  public SyntaxToken closeParenthesis() {
    return closeParenthesis;
  }

  @Override
  public Kind getKind() {
    return kind;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>concat(parameters.iterator());
  }

}
