/*
 * Copyright 2009-2019 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.js2flowgraph.asttraversals;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

public abstract class DispatchingParseTreeAuxVisitor<V, Aux> implements ParseTreeAuxVisitor<V, Aux> {

    /**
     * From {@link com.google.javascript.jscomp.parsing.IRFactory.TransformDispatcher#process(ParseTree)}
     */
    public V process(ParseTree node, Aux aux) {
        switch (node.type) {
            case BINARY_OPERATOR:
                return process(node.asBinaryOperator(), aux);
            case ARRAY_LITERAL_EXPRESSION:
                return process(node.asArrayLiteralExpression(), aux);
            case TEMPLATE_LITERAL_EXPRESSION:
                return process(node.asTemplateLiteralExpression(), aux);
            case TEMPLATE_LITERAL_PORTION:
                return process(node.asTemplateLiteralPortion(), aux);
            case TEMPLATE_SUBSTITUTION:
                return process(node.asTemplateSubstitution(), aux);
            case UNARY_EXPRESSION:
                return process(node.asUnaryExpression(), aux);
            case BLOCK:
                return process(node.asBlock(), aux);
            case BREAK_STATEMENT:
                return process(node.asBreakStatement(), aux);
            case CALL_EXPRESSION:
                return process(node.asCallExpression(), aux);
            case CASE_CLAUSE:
                return process(node.asCaseClause(), aux);
            case DEFAULT_CLAUSE:
                return process(node.asDefaultClause(), aux);
            case CATCH:
                return process(node.asCatch(), aux);
            case CONTINUE_STATEMENT:
                return process(node.asContinueStatement(), aux);
            case DO_WHILE_STATEMENT:
                return process(node.asDoWhileStatement(), aux);
            case EMPTY_STATEMENT:
                return process(node.asEmptyStatement(), aux);
            case EXPRESSION_STATEMENT:
                return process(node.asExpressionStatement(), aux);
            case DEBUGGER_STATEMENT:
                return process(node.asDebuggerStatement(), aux);
            case THIS_EXPRESSION:
                return process(node.asThisExpression(), aux);
            case FOR_STATEMENT:
                return process(node.asForStatement(), aux);
            case FOR_IN_STATEMENT:
                return process(node.asForInStatement(), aux);
            case FUNCTION_DECLARATION:
                return process(node.asFunctionDeclaration(), aux);
            case MEMBER_LOOKUP_EXPRESSION:
                return process(node.asMemberLookupExpression(), aux);
            case MEMBER_EXPRESSION:
                return process(node.asMemberExpression(), aux);
            case CONDITIONAL_EXPRESSION:
                return process(node.asConditionalExpression(), aux);
            case IF_STATEMENT:
                return process(node.asIfStatement(), aux);
            case LABELLED_STATEMENT:
                return process(node.asLabelledStatement(), aux);
            case PAREN_EXPRESSION:
                return process(node.asParenExpression(), aux);
            case IDENTIFIER_EXPRESSION:
                return process(node.asIdentifierExpression(), aux);
            case NEW_EXPRESSION:
                return process(node.asNewExpression(), aux);
            case OBJECT_LITERAL_EXPRESSION:
                return process(node.asObjectLiteralExpression(), aux);
            case COMPUTED_PROPERTY_DEFINITION:
                return process(node.asComputedPropertyDefinition(), aux);
            case COMPUTED_PROPERTY_GETTER:
                return process(node.asComputedPropertyGetter(), aux);
            case COMPUTED_PROPERTY_MEMBER_VARIABLE:
                return process(node.asComputedPropertyMemberVariable(), aux);
            case COMPUTED_PROPERTY_METHOD:
                return process(node.asComputedPropertyMethod(), aux);
            case COMPUTED_PROPERTY_SETTER:
                return process(node.asComputedPropertySetter(), aux);
            case RETURN_STATEMENT:
                return process(node.asReturnStatement(), aux);
            case UPDATE_EXPRESSION:
                return process(node.asUpdateExpression(), aux);
            case PROGRAM:
                return process(node.asProgram(), aux);
            case LITERAL_EXPRESSION: // STRING, NUMBER, TRUE, FALSE, NULL, REGEXP
                return process(node.asLiteralExpression(), aux);
            case SWITCH_STATEMENT:
                return process(node.asSwitchStatement(), aux);
            case THROW_STATEMENT:
                return process(node.asThrowStatement(), aux);
            case TRY_STATEMENT:
                return process(node.asTryStatement(), aux);
            case VARIABLE_STATEMENT: // var const let
                return process(node.asVariableStatement(), aux);
            case VARIABLE_DECLARATION_LIST:
                return process(node.asVariableDeclarationList(), aux);
            case VARIABLE_DECLARATION:
                return process(node.asVariableDeclaration(), aux);
            case WHILE_STATEMENT:
                return process(node.asWhileStatement(), aux);
            case WITH_STATEMENT:
                return process(node.asWithStatement(), aux);

            case COMMA_EXPRESSION:
                return process(node.asCommaExpression(), aux);
            case NULL: // this is not the null literal
                return process(node.asNull(), aux);
            case FINALLY:
                return process(node.asFinally(), aux);

            case MISSING_PRIMARY_EXPRESSION:
                return process(node.asMissingPrimaryExpression(), aux);

            case PROPERTY_NAME_ASSIGNMENT:
                return process(node.asPropertyNameAssignment(), aux);
            case GET_ACCESSOR:
                return process(node.asGetAccessor(), aux);
            case SET_ACCESSOR:
                return process(node.asSetAccessor(), aux);
            case FORMAL_PARAMETER_LIST:
                return process(node.asFormalParameterList(), aux);

            case CLASS_DECLARATION:
                return process(node.asClassDeclaration(), aux);
            case SUPER_EXPRESSION:
                return process(node.asSuperExpression(), aux);
            case NEW_TARGET_EXPRESSION:
                return process(node.asNewTargetExpression(), aux);
            case YIELD_EXPRESSION:
                return process(node.asYieldStatement(), aux);
            case AWAIT_EXPRESSION:
                return process(node.asAwaitExpression(), aux);
            case FOR_OF_STATEMENT:
                return process(node.asForOfStatement(), aux);

            case EXPORT_DECLARATION:
                return process(node.asExportDeclaration(), aux);
            case EXPORT_SPECIFIER:
                return process(node.asExportSpecifier(), aux);
            case IMPORT_DECLARATION:
                return process(node.asImportDeclaration(), aux);
            case IMPORT_SPECIFIER:
                return process(node.asImportSpecifier(), aux);

            case ARRAY_PATTERN:
                return process(node.asArrayPattern(), aux);
            case OBJECT_PATTERN:
                return process(node.asObjectPattern(), aux);
            case ASSIGNMENT_REST_ELEMENT:
                return process(node.asAssignmentRestElement(), aux);

            case COMPREHENSION:
                return process(node.asComprehension(), aux);
            case COMPREHENSION_FOR:
                return process(node.asComprehensionFor(), aux);
            case COMPREHENSION_IF:
                return process(node.asComprehensionIf(), aux);

            case DEFAULT_PARAMETER:
                return process(node.asDefaultParameter(), aux);
            case REST_PARAMETER:
                return process(node.asRestParameter(), aux);
            case SPREAD_EXPRESSION:
                return process(node.asSpreadExpression(), aux);

            // ES6 Typed
            case TYPE_NAME:
                return process(node.asTypeName(), aux);
            case TYPED_PARAMETER:
                return process(node.asTypedParameter(), aux);
            case OPTIONAL_PARAMETER:
                return process(node.asOptionalParameter(), aux);
            case PARAMETERIZED_TYPE_TREE:
                return process(node.asParameterizedType(), aux);
            case ARRAY_TYPE:
                return process(node.asArrayType(), aux);
            case RECORD_TYPE:
                return process(node.asRecordType(), aux);
            case UNION_TYPE:
                return process(node.asUnionType(), aux);
            case FUNCTION_TYPE:
                return process(node.asFunctionType(), aux);
            case TYPE_QUERY:
                return process(node.asTypeQuery(), aux);
            case GENERIC_TYPE_LIST:
                return process(node.asGenericTypeList(), aux);
            case MEMBER_VARIABLE:
                return process(node.asMemberVariable(), aux);

            case INTERFACE_DECLARATION:
                return process(node.asInterfaceDeclaration(), aux);
            case NAMESPACE_NAME:
                break;
            case ENUM_DECLARATION:
                return process(node.asEnumDeclaration(), aux);

            case TYPE_ALIAS:
                return process(node.asTypeAlias(), aux);
            case AMBIENT_DECLARATION:
                return process(node.asAmbientDeclaration(), aux);
            case NAMESPACE_DECLARATION:
                return process(node.asNamespaceDeclaration(), aux);

            case INDEX_SIGNATURE:
                return process(node.asIndexSignature(), aux);
            case CALL_SIGNATURE:
                return process(node.asCallSignature(), aux);

            // TODO(johnlenz): handle these or remove parser support
            case MODULE_IMPORT:
                break;
            case ARGUMENT_LIST:
                break;
            default:
                return unsupportedLanguageFeature(node, node.type.name());
        }
        return ignoredByClosureCompiler(node);
    }

}
