/*
 * Copyright 2009-2013 Aarhus University
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

package dk.brics.tajs.htmlparser;

import org.jdom.Element;

/**
 * Visitor interface for JDOM HTML document traversal.
 */
public interface HTMLVisitor {

    public void visitDocument();

    public void visit(Element element);

    public void visitA(Element element);

    public void visitApplet(Element element);

    public void visitArea(Element element);

    public void visitBase(Element element);

    public void visitBaseFont(Element element);

    public void visitBR(Element element);

    public void visitBody(Element element);

    public void visitButton(Element element);

    public void visitCaption(Element element);

    public void visitCol(Element element);

    public void visitDir(Element element);

    public void visitDiv(Element element);

    public void visitDl(Element element);

    public void visitFieldSet(Element element);

    public void visitFont(Element element);

    public void visitForm(Element element);

    public void visitFrame(Element element);

    public void visitFrameSet(Element element);

    public void visitH1(Element element);

    public void visitH2(Element element);

    public void visitH3(Element element);

    public void visitH4(Element element);

    public void visitH5(Element element);

    public void visitH6(Element element);

    public void visitHead(Element element);

    public void visitHR(Element element);

    public void visitHTML(Element element);

    public void visitIFrame(Element element);

    public void visitImg(Element element);

    public void visitInput(Element element);

    public void visitLabel(Element element);

    public void visitLegend(Element element);

    public void visitLI(Element element);

    public void visitLink(Element element);

    public void visitMap(Element element);

    public void visitMenu(Element element);

    public void visitMeta(Element element);

    public void visitObject(Element element);

    public void visitOL(Element element);

    public void visitOptGroup(Element element);

    public void visitOption(Element element);

    public void visitP(Element element);

    public void visitParam(Element element);

    public void visitPre(Element element);

    public void visitScript(Element element);

    public void visitSelect(Element element);

    public void visitStyle(Element element);

    public void visitTable(Element element);

    public void visitTBody(Element element);

    public void visitTD(Element element);

    public void visitTextArea(Element element);

    public void visitTFoot(Element element);

    public void visitTH(Element element);

    public void visitTHead(Element element);

    public void visitTitle(Element element);

    public void visitTR(Element element);

    public void visitUL(Element element);
}
