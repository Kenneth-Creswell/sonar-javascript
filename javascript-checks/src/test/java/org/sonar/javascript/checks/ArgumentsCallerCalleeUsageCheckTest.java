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

import org.junit.Test;
import org.sonar.javascript.checks.utils.TreeCheckTest;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class ArgumentsCallerCalleeUsageCheckTest extends TreeCheckTest {

  @Test
  public void test() {
    SourceFile file = scanFile("src/test/resources/checks/argumentsCallerCalleeUsage.js", new ArgumentsCallerCalleeUsageCheck());

    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(2).withMessage("Name the enclosing function instead of using the deprecated property \"arguments.callee\".")
      .next().atLine(3).withMessage("Remove this use of \"arguments.caller\".")
      .next().atLine(6).withMessage("Remove this use of \"f.caller\".")
      .next().atLine(7).withMessage("Remove this use of \"f.arguments\".")
      .next().atLine(11)
      .next().atLine(13)
      .next().atLine(22)
      .noMore();
  }

}
