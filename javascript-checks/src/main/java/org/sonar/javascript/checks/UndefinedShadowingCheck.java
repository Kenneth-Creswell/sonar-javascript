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
package org.sonar.javascript.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.javascript.api.SymbolModel;
import org.sonar.javascript.ast.resolve.Scope;
import org.sonar.javascript.ast.resolve.Symbol;
import org.sonar.plugins.javascript.api.visitors.BaseTreeVisitor;
import org.sonar.plugins.javascript.api.tree.declaration.ScriptTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import java.util.Collection;

@Rule(
  key = "S2137",
  name = "Local variables should not shadow \"undefined\"",
  priority = Priority.CRITICAL,
  tags = {Tags.PITFALL})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.INSTRUCTION_RELIABILITY)
@SqaleConstantRemediation("10min")
public class UndefinedShadowingCheck extends BaseTreeVisitor {

  private static final String MESSAGE = "Rename this variable.";

  @Override
  public void visitScript(ScriptTree tree) {
    SymbolModel symbolModel = getContext().getSymbolModel();
    Collection<Symbol> symbols = symbolModel.getSymbols("undefined");
    for (Symbol symbol : symbols){
      Scope scope = symbol.scope();
      if (scope.isGlobal()){
        continue;
      }

      if (symbol.is(Symbol.Kind.VARIABLE)){
        getContext().addIssue(this, symbol.declaration().tree(), MESSAGE);
      }
    }

  }
}
