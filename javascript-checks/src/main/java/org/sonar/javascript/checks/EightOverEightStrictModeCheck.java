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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.SonarException;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.javascript.CharsetAwareVisitor;
import org.sonar.javascript.lexer.EcmaScriptLexer;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import com.google.common.io.Files;
import com.sonar.sslr.api.AstNode;

@Rule(
  key = "EightOverEightStrictMode",
  name = "Files should be in strict mode",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_RELIABILITY)
@SqaleConstantRemediation("1min")
public class EightOverEightStrictModeCheck extends SquidCheck<LexerlessGrammar> implements CharsetAwareVisitor {

	private Charset charset;

	@Override
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	@Override
    public void visitFile(AstNode astNode) {
		List<String> lines;
		try {
			lines = Files.readLines(getContext().getFile(), charset);
		} catch (IOException e) {
			throw new SonarException(e);
		}
		if(!ContainsUseStrict(lines)) {
			getContext().createFileViolation(this, "Add 'use strict;' at the start of this file.");
		}
	}
	
	private boolean ContainsUseStrict(List<String> lines) {
		String useStrict = "'use strict';";
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.length() > 0 && line.equals(useStrict)) {
				return true;
			}
		}
		return false;
	}
}
