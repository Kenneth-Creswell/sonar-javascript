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

import com.google.common.collect.ImmutableSet;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.javascript.api.SymbolModel;
import org.sonar.javascript.ast.resolve.Symbol;
import org.sonar.javascript.ast.visitors.BaseTreeVisitor;
import org.sonar.plugins.javascript.api.tree.declaration.ScriptTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import java.util.Set;

@Rule(
  key = "FutureReservedWords",
  name = "\"future reserved words\" should not be used as identifiers",
  priority = Priority.CRITICAL,
  tags = {Tags.LOCK_IN, Tags.PITFALL})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LANGUAGE_RELATED_PORTABILITY)
@SqaleConstantRemediation("5min")
public class FutureReservedWordsCheck extends BaseTreeVisitor {

  private static final String MESSAGE = "Rename \"%s\" identifier to prevent potential conflicts with future evolutions of the JavaScript language.";

  private static final Set<String> FUTURE_RESERVED_WORDS = ImmutableSet.of(
      "implements",
      "interface",
      "package",
      "private",
      "protected",
      "public",
      "enum",
      "class",
      "const",
      "export",
      "extends",
      "import",
      "super",
      "let",
      "static",
      "yield",
      "await"
  );

  @Override
  public void visitScript(ScriptTree tree) {
    SymbolModel symbolModel = getContext().getSymbolModel();
    for (Symbol symbol : symbolModel.getSymbols()) {
      if (FUTURE_RESERVED_WORDS.contains(symbol.name())) {
        getContext().addIssue(this, symbol.declaration().tree(), String.format(MESSAGE, symbol.name()));
      }
    }
  }

}
