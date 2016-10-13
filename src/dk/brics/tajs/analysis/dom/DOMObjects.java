/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;

import java.util.Collections;

/**
 * Native DOM objects.
 */
public enum DOMObjects implements HostObject {

    // /////////////////////////////////////////////////////////////////////////
    // / WINDOW ///
    // /////////////////////////////////////////////////////////////////////////
    // DOM LEVEL 0: WINDOW HISTORY
    WINDOW_HISTORY("Window.history"),
    WINDOW_HISTORY_BACK("Window.history.back"),
    WINDOW_HISTORY_FORWARD("Window.history.forward"),
    WINDOW_HISTORY_GO("Window.history.go"),

    // DOM LEVEL 0: WINDOW LOCATION
    WINDOW_LOCATION("Window.location"),
    WINDOW_LOCATION_ASSIGN("Window.location.assign"),
    WINDOW_LOCATION_RELOAD("Window.location.reload"),
    WINDOW_LOCATION_REPLACE("Window.location.replace"),
    WINDOW_LOCATION_TOSTRING("Window.location.toString"),

    // DOM LEVEL 0: WINDOW NAVIGATOR
    WINDOW_NAVIGATOR("Navigator"),

    // DOM LEVEL 0: WINDOW SCREEN
    WINDOW_SCREEN("Window.screen"),

    // DOM LEVEL 0: WINDOW MISC
    WINDOW_ALERT("Window.alert"),
    WINDOW_ATOB("Window.atob"),
    WINDOW_BACK("Window.back"),
    WINDOW_BLUR("Window.blur"),
    WINDOW_BTOA("Window.btoa"),
    WINDOW_CLEAR_INTERVAL("Window.clearInterval"),
    WINDOW_CLEAR_TIMEOUT("Window.clearTimeout"),
    WINDOW_CLOSE("Window.close"),
    WINDOW_CONFIRM("Window.confirm"),
    WINDOW_ESCAPE("Window.escape"),
    WINDOW_FOCUS("Window.focus"),
    WINDOW_FORWARD("Window.forward"),
    WINDOW_HOME("Window.home"),
    WINDOW_MAXIMIZE("Window.maximize"),
    WINDOW_MINIMIZE("Window.minimize"),
    WINDOW_MOVEBY("Window.moveBy"),
    WINDOW_MOVETO("Window.moveTo"),
    WINDOW_OPEN("Window.open"),
    WINDOW_PRINT("Window.print"),
    WINDOW_PROMPT("Window.prompt"),
    WINDOW_RESIZEBY("Window.resizeBy"),
    WINDOW_RESIZETO("Window.resizeTo"),
    WINDOW_SCROLL("Window.scroll"),
    WINDOW_SCROLLBY("Window.scrollBy"),
    WINDOW_SCROLLBYLINES("Window.scrollByLines"),
    WINDOW_SCROLLBYPAGES("Window.scrollByPages"),
    WINDOW_SCROLLTO("Window.scrollTo"),
    WINDOW_SET_INTERVAL("Window.setInterval"),
    WINDOW_SET_TIMEOUT("Window.setTimeout"),
    WINDOW_STOP("Window.stop"),
    WINDOW_UNESCAPE("Window.unescape"),

    // DOM LEVEL 2
    WINDOW_GET_COMPUTED_STYLE("Window.getComputedStyle"),

    // DOM LEVEL 0: addEventListener / removeEventListener
    WINDOW_ADD_EVENT_LISTENER("Window.addEventListener"),
    WINDOW_REMOVE_EVENT_LISTENER("Window.removeEventListener"),

    // Window JSON
    WINDOW_JSON("Window.JSON"), // TODO: JSON.parse is in ES5
    WINDOW_JSON_PARSE("Window.JSON.parse"),

    // /////////////////////////////////////////////////////////////////////////
    // / DOCUMENT ///
    // /////////////////////////////////////////////////////////////////////////

    // DOM LEVEL 1
    DOCUMENT_CONSTRUCTOR("Document constructor"),
    DOCUMENT_PROTOTYPE("Document.prototype"),
    DOCUMENT_INSTANCES("Document instances"),
    DOCUMENT_CREATEPROCESSINGINSTRUCTION("Document.createProcessingInstruction"),
    DOCUMENT_CREATE_ATTRIBUTE("Document.createAttribute"),
    DOCUMENT_CREATE_CDATASECTION("Document.createCDATASection"),
    DOCUMENT_CREATE_COMMENT("Document.createComment"),
    DOCUMENT_CREATE_DOCUMENTFRAGMENT("Document.createDocumentFragment"),
    DOCUMENT_CREATE_ELEMENT("Document.createElement"),
    DOCUMENT_CREATE_ENTITYREFERENCE("Document.createEntityReference"),
    DOCUMENT_CREATE_TEXTNODE("Document.createTextNode"),
    DOCUMENT_GET_ELEMENTS_BY_TAGNAME("Document.getElementsByTagName"),

    // DOM LEVEL 2
    DOCUMENT_CREATE_ATTRIBUTE_NS("Document.createAttributeNS"),
    DOCUMENT_CREATE_ELEMENT_NS("Document.createElementNS"),
    DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS("Document.getElementsByTagNameNS"),
    DOCUMENT_GET_ELEMENT_BY_ID("Document.getElementById"),
    DOCUMENT_IMPORT_NODE("Document.importNode"),

    // DOM LEVEL 3
    DOCUMENT_RENAME_NODE("Document.renameNode"),
    DOCUMENT_NORMALIZEDOCUMENT("Document.normalizeDocument"),
    DOCUMENT_ADOPT_NODE("Document.adoptNode"),

    // semistandard
    DOCUMENT_QUERY_SELECTOR_ALL("Document.querySelectorAll"),

    // /////////////////////////////////////////////////////////////////////////
    // / EVENT ///
    // /////////////////////////////////////////////////////////////////////////

    EVENT_CONSTRUCTOR("Event constructor"),
    EVENT_PROTOTYPE("Event.prototype"),
    EVENT_INSTANCES("Event instances"),
    EVENT_STOP_PROPAGATION("Event.stopPropagation"),
    EVENT_PREVENT_DEFAULT("Event.preventDefault"),
    EVENT_INIT_EVENT("Event.initEvent"),
    EVENT_TARGET("EventTarget"),
    EVENT_TARGET_PROTOTYPE("EventTarget.prototype"),
    EVENT_TARGET_ADD_EVENT_LISTENER("EventTarget.addEventListener"),
    EVENT_TARGET_REMOVE_EVENT_LISTENER("EventTarget.removeEventListener"),
    EVENT_TARGET_DISPATCH_EVENT("EventTarget.dispatchEvent"),
    EVENT_LISTENER_INSTANCES("EventListener"),
    EVENT_LISTENER_PROTOTYPE("EventListener.prototype"),
    EVENT_LISTENER_HANDLE_EVENT("EventListener.handleEvent"),
    EVENT_EXCEPTION_CONSTRUCTOR("EventException constructor"),
    EVENT_EXCEPTION_PROTOTYPE("EventException.prototype"),
    EVENT_EXCEPTION_INSTANCES("EventException instances"),
    DOCUMENT_EVENT("DocumentEvent"),
    DOCUMENT_EVENT_PROTOTYPE("DocumentEvent.prototype"),
    DOCUMENT_EVENT_CREATE_EVENT("DocumentEvent.createEvent"),
    UI_EVENT_INSTANCES("UIEvent"),
    UI_EVENT_PROTOTYPE("UIEvent.prototype"),
    UI_EVENT_INIT_UI_EVENT("UIEvent.initUIEvent"),
    MOUSE_EVENT_INSTANCES("MouseEvent"),
    MOUSE_EVENT_PROTOTYPE("MouseEvent.prototype"),
    MOUSE_EVENT_INIT_MOUSE_EVENT("MouseEvent.initMouseEvent"),
    KEYBOARD_EVENT_INSTANCES("KeyboardEvent"),
    KEYBOARD_EVENT_PROTOTYPE("KeyboardEvent.prototype"),
    KEYBOARD_EVENT_GET_MODIFIER_STATE("KeyboardEvent.getModifierState"),
    KEYBOARD_EVENT_INIT_KEYBOARD_EVENT("KeyboardEvent.initKeyboardEvent"),
    KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS("KeyboardEvent.initKeyboardEventNS"),
    MUTATION_EVENT_INSTANCES("MutationEvent"),
    MUTATION_EVENT_PROTOTYPE("MutationEvent.prototype"),
    MUTATION_EVENT_INIT_MUTATION_EVENT("MutationEvent.initMutationEvent"),
    WHEEL_EVENT_INSTANCES("WheelEvent"),
    WHEEL_EVENT_INIT_WHEEL_EVENT("WheelEvent.initWheelEvent"),
    WHEEL_EVENT_INIT_WHEEL_EVENT_NS("WheelEvent.initWheelEventNS"),
    WHEEL_EVENT_PROTOTYPE("WheelEvent.prototype"),
    TOUCH_EVENT_INSTANCES("TouchEvent"),
    TOUCH_EVENT_PROTOTYPE("TouchEvent.prototype"),

    LOAD_EVENT_PROTOTYPE("LoadEvent.prototype"),
    LOAD_EVENT_INSTANCES("LoadEvent instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / VIEWS ///
    // /////////////////////////////////////////////////////////////////////////
    ABSTRACT_VIEW("AbstractView"),

    // /////////////////////////////////////////////////////////////////////////
    // / DOM CORE ///
    // /////////////////////////////////////////////////////////////////////////
    ELEMENT_CONSTRUCTOR("Element constructor"),
    ELEMENT_PROTOTYPE("Element.prototype"),
    ELEMENT_INSTANCES("Element instances"),
    ELEMENT_GET_ATTRIBUTE("Element.getAttribute"),
    ELEMENT_GET_ATTRIBUTE_NS("Element.getAttributeNS"),
    ELEMENT_GET_ATTRIBUTE_NODE("Element.getAttributeNode"),
    ELEMENT_GET_ATTRIBUTE_NODE_NS("Element.getAttributeNodeNS"),
    ELEMENT_GET_BOUNDING_CLIENT_RECT("Element.getBoundingClientRect"),
    ELEMENT_GET_ELEMENTS_BY_TAGNAME("Element.getElementsByTagName"),
    ELEMENT_GET_ELEMENTS_BY_TAGNAME_NS("Element.getElementsByTagNameNS"),
    ELEMENT_QUERY_SELECTOR_ALL("Element.querySelectorAll"),
    ELEMENT_HAS_ATTRIBUTE("Element.hasAttribute"),
    ELEMENT_HAS_ATTRIBUTE_NS("Element.hasAttributeNS"),
    ELEMENT_REMOVE_ATTRIBUTE("Element.removeAttribute"),
    ELEMENT_REMOVE_ATTRIBUTE_NS("Element.removeAttributeNS"),
    ELEMENT_REMOVE_ATTRIBUTE_NODE("Element.removeAttributeNode"),
    ELEMENT_SET_ATTRIBUTE("Element.setAttribute"),
    ELEMENT_SET_ATTRIBUTE_NS("Element.setAttributeNS"),
    ELEMENT_SET_ATTRIBUTE_NODE("Element.setAttributeNode"),
    ELEMENT_SET_ATTRIBUTE_NODE_NS("Element.setAttributeNodeNS"),
    ELEMENT_SET_ID_ATTRIBUTE("Element.setIdAttributeNode"),
    ELEMENT_SET_ID_ATTRIBUTE_NS("Element.setIdAttributeNodeNS"),
    ELEMENT_SET_ID_ATTRIBUTE_NODE("Element.setIdAttributeNodeNode"),

    ATTR_CONSTRUCTOR("Attr constructor"),
    ATTR_PROTOTYPE("Attr.prototype"),
    ATTR_INSTANCES("Attr instances"),
    CDATASECTION_CONSTRUCTOR("CDATASection constructor"),
    CDATASECTION_PROTOTYPE("CDATASection.prototype"),
    CDATASECTION_INSTANCES("CDATASection instances"),
    COMMENT_CONSTRUCTOR("Comment constructor"),
    COMMENT_PROTOTYPE("Comment.prototype"),
    COMMENT_INSTANCES("Comment instances"),
    CHARACTERDATA_CONSTRUCTOR("CharacterData constructor"),
    CHARACTERDATA_PROTOTYPE("CharacterData.prototype"),
    CHARACTERDATA_INSTANCES("CharacterData instances"),
    CHARACTERDATA_SUBSTRINGDATA("CharacterData.substringData"),
    CHARACTERDATA_APPENDDATA("CharacterData.appendData"),
    CHARACTERDATA_INSERTDATA("CharacterData.insertData"),
    CHARACTERDATA_DELETEDATA("CharacterData.deleteData"),
    CHARACTERDATA_REPLACEDATA("CharacterData.replaceData"),
    DOCUMENTTYPE_CONSTRUCTOR("DocumentType constructor"),
    DOCUMENTTYPE_PROTOTYPE("DocumentType.prototype"),
    DOCUMENTTYPE_INSTANCES("DocumentType instances"),
    DOCUMENTFRAGMENT_CONSTRUCTOR("DocumentFragment constructor"),
    DOCUMENTFRAGMENT_PROTOTYPE("DocumentFragment.prototype"),
    DOCUMENTFRAGMENT_INSTANCES("DocumentFragment instances"),
    ENTITY_CONSTRUCTOR("Entity constructor"),
    ENTITY_PROTOTYPE("Entity.prototype"),
    ENTITY_INSTANCES("Entity instances"),
    ENTITYREFERENCE_CONSTRUCTOR("EntityReference constructor"),
    ENTITYREFERENCE_PROTOTYPE("EntityReference.prototype"),
    ENTITYREFERENCE_INSTANCES("EntityReference instances"),

    NODE_CONSTRUCTOR("Node constructor"),
    NODE_PROTOTYPE("Node.prototype"),
    NODE_INSTANCES("Node instances"),
    NODE_APPEND_CHILD("Node.appendChild"),
    NODE_CLONE_NODE("Node.cloneNode"),
    NODE_INSERT_BEFORE("Node.insertBefore"),
    NODE_HAS_CHILD_NODES("Node.hasChildNodes"),
    NODE_REMOVE_CHILD("Node.removeChild"),
    NODE_REPLACE_CHILD("Node.replaceChild"),
    NODE_IS_SUPPORTED("Node.isSupported"),
    NODE_HAS_ATTRIBUTES("Node.hasAttributes"),
    NODE_NORMALIZE("Node.normalize"),
    NODE_COMPARE_DOCUMENT_POSITION("compareDocumentPosition"),
    NODE_CONTAINS("contains"),

    NODELIST_CONSTRUCTOR("NodeList constructor"),
    NODELIST_PROTOTYPE("NodeList.prototype"),
    NODELIST_INSTANCES("NodeList instances"),
    NODELIST_ITEM("NodeList.item"),

    PROCESSINGINSTRUCTION_CONSTRUCTOR("ProcessingInstruction constructor"),
    PROCESSINGINSTRUCTION_PROTOTYPE("ProcessingInstruction.prototype"),
    PROCESSINGINSTRUCTION_INSTANCES("ProcessingInstruction instances"),
    CONFIGURATION_CONSTRUCTOR("DOMConfiguration constructor"),
    CONFIGURATION_PROTOTYPE("DOMConfiguration.prototype"),
    CONFIGURATION_INSTANCES("DOMConfiguration instances"),
    CONFIGURATION_GET_PARAMETER("DOMConfiguration.getParameter"),
    CONFIGURATION_SET_PARAMETER("DOMConfiguration.setParameter"),
    CONFIGURATION_CAN_SET_PARAMETER("DOMConfiguration.canSetParameter"),
    DOMEXCEPTION("DOMException"),
    DOMEXCEPTION_PROTOTYPE("DOMException.prototype"),
    DOMIMPLEMENTATION_CONSTRUCTOR("DOMImplementation constructor"),
    DOMIMPLEMENTATION_PROTOTYPE("DOMImplementation.prototype"),
    DOMIMPLEMENTATION_INSTANCES("DOMImplementation instances"),
    DOMIMPLEMENTATION_HASFEATURE("hasFeature"),
    DOMIMPLEMENTATION_CREATEDOCUMENTTYPE("createDocumentType"),
    DOMIMPLEMENTATION_CREATEDOCUMENT("createDocument"),
    NAMEDNODEMAP_CONSTRUCTOR("NamedNodeMap constructor"),
    NAMEDNODEMAP_PROTOTYPE("NamedNodeMap.prototype"),
    NAMEDNODEMAP_INSTANCES("NamedNodeMap instances"),
    NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM("NamedNodeMap.getNamedItem"),
    NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM("NamedNodeMap.setNamedItem"),
    NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM("NamedNodeMap.removeNamedItem"),
    NAMEDNODEMAP_PROTOTYPE_ITEM("NamedNodeMap.item"),
    NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS("NamedNodeMap.getNamedItemNS"),
    NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS("NamedNodeMap.setNamedItemNS"),
    NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS("NamedNodeMap.removedNamedItemNS"),
    TEXT_CONSTRUCTOR("Text constructor"),
    TEXT_PROTOTYPE("Text.prototype"),
    TEXT_INSTANCES("Text instances"),
    TEXT_SPLIT_TEXT("Text.prototype.splitText"),
    TEXT_REPLACE_WHOLE_TEXT("Text.prototype.replaceWholeText"),
    NOTATION_CONSTRUCTOR("Notation constructor"),
    NOTATION_PROTOTYPE("Notation.prototype"),
    NOTATION_INSTANCES("Notation instances"),

    STRINGLIST_CONSTRUCTOR("StringList constructor"),
    STRINGLIST_PROTOTYPE("StringList.prototype"),
    STRINGLIST_INSTANCES("StringList instances"),
    STRINGLIST_CONTAINS("StringList.contains"),
    STRINGLIST_ITEM("StringList.item"),

    TOUCHLIST_CONSTRUCTOR("TouchList constructor"),
    TOUCHLIST_PROTOTYPE("TouchList.prototype"),
    TOUCHLIST_INSTANCES("TouchList instances"),
    TOUCHLIST_ITEM("TouchList.item"),

    TOUCH_CONSTRUCTOR("Touch constructor"),
    TOUCH_PROTOTYPE("Touch.prototype"),
    TOUCH_INSTANCES("Touch instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML OBJECTS ///
    // /////////////////////////////////////////////////////////////////////////
    HTMLANCHORELEMENT_CONSTRUCTOR("HTMLAnchorElement constructor"),
    HTMLANCHORELEMENT_PROTOTYPE("HTMLAnchorElement.prototype"),
    HTMLANCHORELEMENT_INSTANCES("HTMLAnchorElement instances"),
    HTMLANCHORELEMENT_BLUR("HTMLAnchorElement.prototype.blur"),
    HTMLANCHORELEMENT_FOCUS("HTMLAnchorElement.prototype.focus"),
    HTMLAPPLETELEMENT_CONSTRUCTOR("HTMLAppletElement constructor"),
    HTMLAPPLETELEMENT_PROTOTYPE("HTMLAppletElement.prototype"),
    HTMLAPPLETELEMENT_INSTANCES("HTMLAppletElement instances"),
    HTMLAREAELEMENT_CONSTRUCTOR("HTMLAreaElement constructor"),
    HTMLAREAELEMENT_PROTOTYPE("HTMLAreaElement.prototype"),
    HTMLAREAELEMENT_INSTANCES("HTMLAreaElement instances"),
    HTMLBRELEMENT_CONSTRUCTOR("HTMLBRElement constructor"),
    HTMLBRELEMENT_PROTOTYPE("HTMLBRElement.prototype"),
    HTMLBRELEMENT_INSTANCES("HTMLBRElement instances"),
    HTMLBASEELEMENT_CONSTRUCTOR("HTMLBaseElement constructor"),
    HTMLBASEELEMENT_PROTOTYPE("HTMLBaseElement.prototype"),
    HTMLBASEELEMENT_INSTANCES("HTMLBaseElement instances"),
    HTMLBASEFONTELEMENT_CONSTRUCTOR("HTMLBaseFontElement constructor"),
    HTMLBASEFONTELEMENT_PROTOTYPE("HTMLBaseFontElement.prototype"),
    HTMLBASEFONTELEMENT_INSTANCES("HTMLBaseFontElement instances"),
    HTMLBODYELEMENT_CONSTRUCTOR("HTMLBodyElement constructor"),
    HTMLBODYELEMENT_PROTOTYPE("HTMLBodyElement.prototype"),
    HTMLBODYELEMENT_INSTANCES("HTMLBodyElement instances"),
    HTMLBUTTONELEMENT_CONSTRUCTOR("HTMLButtonElement constructor"),
    HTMLBUTTONELEMENT_PROTOTYPE("HTMLButtonElement.prototype"),
    HTMLBUTTONELEMENT_INSTANCES("HTMLButtonElement instances"),
    HTMLCOLLECTION_CONSTRUCTOR("HTMLCollection constructor"),
    HTMLCOLLECTION_PROTOTYPE("HTMLCollection.prototype"),
    HTMLCOLLECTION_INSTANCES("HTMLCollection instances"),
    HTMLCOLLECTION_ITEM("HTMLCollection.prototype.item"),
    HTMLCOLLECTION_NAMEDITEM("HTMLCollection.prototype.namedItem"),
    HTMLDLISTELEMENT_CONSTRUCTOR("HTMLDListElement constructor"),
    HTMLDLISTELEMENT_PROTOTYPE("HTMLDListElement.prototype"),
    HTMLDLISTELEMENT_INSTANCES("HTMLDListElement instances"),
    HTMLDIRECTORYELEMENT_CONSTRUCTOR("HTMLDirectoryElement constructor"),
    HTMLDIRECTORYELEMENT_PROTOTYPE("HTMLDirectoryElement.prototype"),
    HTMLDIRECTORYELEMENT_INSTANCES("HTMLDirectoryElement instances"),
    HTMLDIVELEMENT_CONSTRUCTOR("HTMLDivElement constructor"),
    HTMLDIVELEMENT_PROTOTYPE("HTMLDivElement.prototype"),
    HTMLDIVELEMENT_INSTANCES("HTMLDivElement instances"),
    HTMLDOCUMENT_CONSTRUCTOR("HTMLDocument constructor"),
    HTMLDOCUMENT_PROTOTYPE("HTMLDocument.prototype"),
    HTMLDOCUMENT_INSTANCES("HTMLDocument instances"),
    HTMLDOCUMENT_OPEN("HTMLDocument.prototype.open"),
    HTMLDOCUMENT_CLOSE("HTMLDocument.prototype.close"),
    HTMLDOCUMENT_WRITE("HTMLDocument.prototype.write"),
    HTMLDOCUMENT_WRITELN("HTMLDocument.prototype.writeln"),
    HTMLDOCUMENT_GET_ELEMENTS_BY_NAME("HTMLDocument.prototype.getElementsByName"),
    HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME("HTMLDocument.prototype.getElementsByClassName"),
    HTMLELEMENT("HTMLElement"),
    HTMLELEMENT_PROTOTYPE("HTMLElement.prototype"),
    HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME("HTMLElement.prototype.getElementsByClassName"),
    HTMLELEMENT_FOCUS("HTMLElement.prototype.focus"),
    HTMLELEMENT_BLUR("HTMLElement.prototype.blur"),
    HTMLELEMENT_MATCHES_SELECTOR("HTMLElement.prototype.(*)MatchesSelector"),
    HTMLFIELDSETELEMENT_CONSTRUCTOR("HTMLFieldsetElement constructor"),
    HTMLFIELDSETELEMENT_PROTOTYPE("HTMLFieldsetElement.prototype"),
    HTMLFIELDSETELEMENT_INSTANCES("HTMLFieldsetElement instances"),
    HTMLFONTELEMENT_CONSTRUCTOR("HTMLFontElement constructor"),
    HTMLFONTELEMENT_PROTOTYPE("HTMLFontElement.prototype"),
    HTMLFONTELEMENT_INSTANCES("HTMLFontElement instances"),
    HTMLFORMELEMENT_CONSTRUCTOR("HTMLFormElement constructor"),
    HTMLFORMELEMENT_PROTOTYPE("HTMLFormElement.prototype"),
    HTMLFORMELEMENT_INSTANCES("HTMLFormElement instances"),
    HTMLFORMELEMENT_SUBMIT("HTMLFormElement.prototype.submit"),
    HTMLFORMELEMENT_RESET("HTMLFormElement.prototype.reset"),
    HTMLFRAMEELEMENT_CONSTRUCTOR("HTMLFrameElement constructor"),
    HTMLFRAMEELEMENT_PROTOTYPE("HTMLFrameElement.prototype"),
    HTMLFRAMEELEMENT_INSTANCES("HTMLFrameElement instances"),
    HTMLFRAMESETELEMENT_CONSTRUCTOR("HTMLFramesetElement constructor"),
    HTMLFRAMESETELEMENT_PROTOTYPE("HTMLFramesetElement.prototype"),
    HTMLFRAMESETELEMENT_INSTANCES("HTMLFramesetElement instances"),
    HTMLHRELEMENT_CONSTRUCTOR("HTMLHRElement constructor"),
    HTMLHRELEMENT_PROTOTYPE("HTMLHRElement.prototype"),
    HTMLHRELEMENT_INSTANCES("HTMLHRElement instances"),
    HTMLHEADELEMENT_CONSTRUCTOR("HTMLHeadElement constructor"),
    HTMLHEADELEMENT_PROTOTYPE("HTMLHeadElement.prototype"),
    HTMLHEADELEMENT_INSTANCES("HTMLHeadElement instances"),
    HTMLHEADINGELEMENT_CONSTRUCTOR("HTMLHeadingElement constructor"),
    HTMLHEADINGELEMENT_PROTOTYPE("HTMLHeadingElement.prototype"),
    HTMLHEADINGELEMENT_INSTANCES("HTMLHeadingElement instances"),
    HTMLHTMLELEMENT_CONSTRUCTOR("HTMLHtmlElement constructor"),
    HTMLHTMLELEMENT_PROTOTYPE("HTMLHtmlElement.prototype"),
    HTMLHTMLELEMENT_INSTANCES("HTMLHtmlElement instances"),
    HTMLIFRAMEELEMENT_CONSTRUCTOR("HTMLIFrameElement constructor"),
    HTMLIFRAMEELEMENT_PROTOTYPE("HTMLIFrameElement.prototype"),
    HTMLIFRAMEELEMENT_INSTANCES("HTMLIFrameElement instances"),
    HTMLIMAGEELEMENT_INSTANCES("HTMLImageElement"),
    HTMLIMAGEELEMENT_PROTOTYPE("HTMLImageElement.prototype"),
    HTMLINPUTELEMENT_CONSTRUCTOR("HTMLInputElement constructor"),
    HTMLINPUTELEMENT_PROTOTYPE("HTMLInputElement.prototype"),
    HTMLINPUTELEMENT_INSTANCES("HTMLInputElement instances"),
    HTMLINPUTELEMENT_BLUR("HTMLInputElement.prototype.blur"),
    HTMLINPUTELEMENT_FOCUS("HTMLInputElement.prototype.focus"),
    HTMLINPUTELEMENT_SELECT("HTMLInputElement.prototype.select"),
    HTMLINPUTELEMENT_CLICK("HTMLInputElement.prototype.click"),
    HTMLISINDEXELEMENT_CONSTRUCTOR("HTMLListIndexElement constructor"),
    HTMLISINDEXELEMENT_PROTOTYPE("HTMLListIndexElement.prototype"),
    HTMLISINDEXELEMENT_INSTANCES("HTMLListIndexElement instances"),
    HTMLLIELEMENT_CONSTRUCTOR("HTMLLIElement constructor"),
    HTMLLIELEMENT_PROTOTYPE("HTMLLIElement.prototype"),
    HTMLLIELEMENT_INSTANCES("HTMLLIElement instances"),
    HTMLLABELELEMENT_CONSTRUCTOR("HTMLLabelElement constructor"),
    HTMLLABELELEMENT_PROTOTYPE("HTMLLabelElement.prototype"),
    HTMLLABELELEMENT_INSTANCES("HTMLLabelElement instances"),
    HTMLLEGENDELEMENT_CONSTRUCTOR("HTMLLegendElement constructor"),
    HTMLLEGENDELEMENT_PROTOTYPE("HTMLLegendElement.prototype"),
    HTMLLEGENDELEMENT_INSTANCES("HTMLLegendElement instances"),
    HTMLLINKELEMENT_CONSTRUCTOR("HTMLLinkElement constructor"),
    HTMLLINKELEMENT_PROTOTYPE("HTMLLinkElement.prototype"),
    HTMLLINKELEMENT_INSTANCES("HTMLLinkElement instances"),
    HTMLMAPELEMENT_CONSTRUCTOR("HTMLMapElement constructor"),
    HTMLMAPELEMENT_PROTOTYPE("HTMLMapElement.prototype"),
    HTMLMAPELEMENT_INSTANCES("HTMLMapElement instances"),
    HTMLMENUELEMENT_CONSTRUCTOR("HTMLMenuElement constructor"),
    HTMLMENUELEMENT_PROTOTYPE("HTMLMenuElement.prototype"),
    HTMLMENUELEMENT_INSTANCES("HTMLMenuElement instances"),
    HTMLMETAELEMENT_CONSTRUCTOR("HTMLMetaElement constructor"),
    HTMLMETAELEMENT_PROTOTYPE("HTMLMetaElement.prototype"),
    HTMLMETAELEMENT_INSTANCES("HTMLMetaElement instances"),
    HTMLMODELEMENT_CONSTRUCTOR("HTMLModeElement constructor"),
    HTMLMODELEMENT_PROTOTYPE("HTMLModeElement.prototype"),
    HTMLMODELEMENT_INSTANCES("HTMLModeElement instances"),
    HTMLOLISTELEMENT_CONSTRUCTOR("HTMLOListElement constructor"),
    HTMLOLISTELEMENT_PROTOTYPE("HTMLOListElement.prototype"),
    HTMLOLISTELEMENT_INSTANCES("HTMLOListElement instances"),
    HTMLOBJECTELEMENT_CONSTRUCTOR("HTMLObjectElement constructor"),
    HTMLOBJECTELEMENT_PROTOTYPE("HTMLObjectElement.prototype"),
    HTMLOBJECTELEMENT_INSTANCES("HTMLObjectElement instances"),
    HTMLOPTGROUPELEMENT_CONSTRUCTOR("HTMLOptGroupElement constructor"),
    HTMLOPTGROUPELEMENT_PROTOTYPE("HTMLOptGroupElement.prototype"),
    HTMLOPTGROUPELEMENT_INSTANCES("HTMLOptGroupElement instances"),
    HTMLOPTIONELEMENT_CONSTRUCTOR("HTMLOptionElement constructor"),
    HTMLOPTIONELEMENT_PROTOTYPE("HTMLOptionElement.prototype"),
    HTMLOPTIONELEMENT_INSTANCES("HTMLOptionElement instances"),
    HTMLOPTIONSCOLLECTION_CONSTRUCTOR("HTMLOptionsCollection constructor"),
    HTMLOPTIONSCOLLECTION_PROTOTYPE("HTMLOptionsCollection.prototype"),
    HTMLOPTIONSCOLLECTION_INSTANCES("HTMLOptionsCollection instances"),
    HTMLOPTIONSCOLLECTION_ITEM("HTMLOptionsCollection.prototype.item"),
    HTMLOPTIONSCOLLECTION_NAMEDITEM("HTMLOptionsCollection.prototype.namedItem"),
    HTMLPARAGRAPHELEMENT_CONSTRUCTOR("HTMLParagraphElement constructor"),
    HTMLPARAGRAPHELEMENT_PROTOTYPE("HTMLParagraphElement.prototype"),
    HTMLPARAGRAPHELEMENT_INSTANCES("HTMLParagraphElement instances"),
    HTMLPARAMELEMENT_CONSTRUCTOR("HTMLParamElement constructor"),
    HTMLPARAMELEMENT_PROTOTYPE("HTMLParamElement.prototype"),
    HTMLPARAMELEMENT_INSTANCES("HTMLParamElement instances"),
    HTMLPREELEMENT_CONSTRUCTOR("HTMLPreElement constructor"),
    HTMLPREELEMENT_PROTOTYPE("HTMLPreElement.prototype"),
    HTMLPREELEMENT_INSTANCES("HTMLPreElement instances"),
    HTMLQUOTEELEMENT_CONSTRUCTOR("HTMLQuoteElement constructor"),
    HTMLQUOTEELEMENT_PROTOTYPE("HTMLQuoteElement.prototype"),
    HTMLQUOTEELEMENT_INSTANCES("HTMLQuoteElement instances"),
    HTMLSCRIPTELEMENT_CONSTRUCTOR("HTMLScriptElement constructor"),
    HTMLSCRIPTELEMENT_PROTOTYPE("HTMLScriptElement.prototype"),
    HTMLSCRIPTELEMENT_INSTANCES("HTMLScriptElement instances"),
    HTMLSELECTELEMENT_CONSTRUCTOR("HTMLSelectElement constructor"),
    HTMLSELECTELEMENT_PROTOTYPE("HTMLSelectElement.prototype"),
    HTMLSELECTELEMENT_INSTANCES("HTMLSelectElement instances"),
    HTMLSELECTELEMENT_ADD("HTMLSelectElement.prototype.add"),
    HTMLSELECTELEMENT_REMOVE("HTMLSelectElement.prototype.remove"),
    HTMLSELECTELEMENT_BLUR("HTMLSelectElement.prototype.blur"),
    HTMLSELECTELEMENT_FOCUS("HTMLSelectElement.prototype.focus"),
    HTMLSTYLEELEMENT_CONSTRUCTOR("HTMLStyleElement constructor"),
    HTMLSTYLEELEMENT_PROTOTYPE("HTMLStyleElement.prototype"),
    HTMLSTYLEELEMENT_INSTANCES("HTMLStyleElement instances"),
    HTMLTABLECAPTIONELEMENT_CONSTRUCTOR("HTMLTableCaptionElement constructor"),
    HTMLTABLECAPTIONELEMENT_PROTOTYPE("HTMLTableCaptionElement.prototype"),
    HTMLTABLECAPTIONELEMENT_INSTANCES("HTMLTableCaptionElement instances"),
    HTMLTABLECELLELEMENT_CONSTRUCTOR("HTMLTableCellElement constructor"),
    HTMLTABLECELLELEMENT_PROTOTYPE("HTMLTableCellElement.prototype"),
    HTMLTABLECELLELEMENT_INSTANCES("HTMLTableCellElement instances"),
    HTMLTABLECOLELEMENT_CONSTRUCTOR("HTMLTableColElement constructor"),
    HTMLTABLECOLELEMENT_PROTOTYPE("HTMLTableColElement.prototype"),
    HTMLTABLECOLELEMENT_INSTANCES("HTMLTableColElement instances"),
    HTMLTABLEELEMENT_CONSTRUCTOR("HTMLTableElement constructor"),
    HTMLTABLEELEMENT_PROTOTYPE("HTMLTableElement.prototype"),
    HTMLTABLEELEMENT_INSTANCES("HTMLTableElement instances"),
    HTMLTABLEELEMENT_CREATETHEAD("HTMLTableElement.prototype.createTHead"),
    HTMLTABLEELEMENT_DELETETHEAD("HTMLTableElement.prototype.deleteTHead"),
    HTMLTABLEELEMENT_CREATETFOOT("HTMLTableElement.prototype.createTFoot"),
    HTMLTABLEELEMENT_DELETETFOOT("HTMLTableElement.prototype.deleteTFoot"),
    HTMLTABLEELEMENT_CREATECAPTION("HTMLTableElement.prototype.createCaption"),
    HTMLTABLEELEMENT_DELETECAPTION("HTMLTableElement.prototype.deleteCaption"),
    HTMLTABLEELEMENT_INSERTROW("HTMLTableElement.prototype.insertRow"),
    HTMLTABLEELEMENT_DELETEROW("HTMLTableElement.prototype.deleteRow"),
    HTMLTABLEROWELEMENT_CONSTRUCTOR("HTMLTableRowElement constructor"),
    HTMLTABLEROWELEMENT_PROTOTYPE("HTMLTableRowElement.prototype"),
    HTMLTABLEROWELEMENT_INSTANCES("HTMLTableRowElement instances"),
    HTMLTABLEROWELEMENT_INSERTCELL("HTMLTableRowElement.prototype.insertCell"),
    HTMLTABLEROWELEMENT_DELETECELL("HTMLTableRowElement.prototype.deleteCell"),
    HTMLTABLESECTIONELEMENT_CONSTRUCTOR("HTMLTableSectionElement constructor"),
    HTMLTABLESECTIONELEMENT_PROTOTYPE("HTMLTableSectionElement.prototype"),
    HTMLTABLESECTIONELEMENT_INSTANCES("HTMLTableSectionElement instances"),
    HTMLTABLESECTIONELEMENT_INSERTROW("HTMLTableSectionElement.prototype.insertRow"),
    HTMLTABLESECTIONELEMENT_DELETEROW("HTMLTableSectionElement.prototype.deleteRow"),
    HTMLTEXTAREAELEMENT_CONSTRUCTOR("HTMLTextAreaElement constructor"),
    HTMLTEXTAREAELEMENT_PROTOTYPE("HTMLTextAreaElement.prototype"),
    HTMLTEXTAREAELEMENT_INSTANCES("HTMLTextAreaElement instances"),
    HTMLTEXTAREAELEMENT_BLUR("HTMLTextAreaElement.prototype.blur"),
    HTMLTEXTAREAELEMENT_FOCUS("HTMLTextAreaElement.prototype.focus"),
    HTMLTEXTAREAELEMENT_SELECT("HTMLTextAreaElement.prototype.select"),
    HTMLTITLEELEMENT_CONSTRUCTOR("HTMLTitleElement constructor"),
    HTMLTITLEELEMENT_PROTOTYPE("HTMLTitleElement.prototype"),
    HTMLTITLEELEMENT_INSTANCES("HTMLTitleElement instances"),
    HTMLULISTELEMENT_CONSTRUCTOR("HTMLUListElement constructor"),
    HTMLULISTELEMENT_PROTOTYPE("HTMLUListElement.prototype"),
    HTMLULISTELEMENT_INSTANCES("HTMLUListElement instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / NON-STANDARD HTML OBJECTS ///
    // /////////////////////////////////////////////////////////////////////////
    HTMLELEMENT_ATTRIBUTES("HTMLElement.attributes"), // DOM LEVEL 0
    HTMLIMAGEELEMENT_CONSTRUCTOR("HTMLImageElement.constructor"), // DOM LEVEL 0

    // /////////////////////////////////////////////////////////////////////////
    // / CSS ///
    // /////////////////////////////////////////////////////////////////////////
    CSSSTYLEDECLARATION("CSSStyleDeclaration"),
    CLIENTBOUNDINGRECT_CONSTRUCTOR("ClientBoundingRect.constructor"),
    CLIENTBOUNDINGRECT_PROTOTYPE("ClientBoundingRect.prototype"),
    CLIENTBOUNDINGRECT_INSTANCES("ClientBoundingRect.instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 CANVAS (WHATWG) ///
    // /////////////////////////////////////////////////////////////////////////
    HTMLCANVASELEMENT_CONSTRUCTOR("HTMLCanvasElement"),
    HTMLCANVASELEMENT_PROTOTYPE("HTMLCanvasElement.prototype"),
    HTMLCANVASELEMENT_INSTANCES("HTMLCanvasElement instances"),
    HTMLCANVASELEMENT_TO_DATA_URL("HTMLCanvasElement.prototype.toDataURL"),
    HTMLCANVASELEMENT_GET_CONTEXT("HTMLCanvasElement.prototype.getContext"),
    CANVASRENDERINGCONTEXT2D("CanvasRenderingContext2D"),
    CANVASRENDERINGCONTEXT2D_PROTOTYPE("CanvasRenderingContext2D.prototype"),

    // State
    CANVASRENDERINGCONTEXT2D_SAVE("CanvasRenderingContext2D.prototype.save"),
    CANVASRENDERINGCONTEXT2D_RESTORE("CanvasRenderingContext2D.prototype.restore"),

    // Transformations
    CANVASRENDERINGCONTEXT2D_SCALE("CanvasRenderingContext2D.prototype.scale"),
    CANVASRENDERINGCONTEXT2D_ROTATE("CanvasRenderingContext2D.prototype.rotate"),
    CANVASRENDERINGCONTEXT2D_TRANSLATE("CanvasRenderingContext2D.prototype.translate"),
    CANVASRENDERINGCONTEXT2D_TRANSFORM("CanvasRenderingContext2D.prototype.transform"),
    CANVASRENDERINGCONTEXT2D_SETTRANSFORM("CanvasRenderingContext2D.prototype.setTransform"),

    // Colors & Styles
    CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT("CanvasRenderingContext2D.prototype.createLinearGradient"),
    CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT("CanvasRenderingContext2D.prototype.createRadialGradient"),
    CANVASRENDERINGCONTEXT2D_CREATE_PATTERN("CanvasRenderingContext2D.prototype.createPattern"),

    // Rects
    CANVASRENDERINGCONTEXT2D_CLEAR_RECT("CanvasRenderingContext2D.prototype.clearRect"),
    CANVASRENDERINGCONTEXT2D_FILL_RECT("CanvasRenderingContext2D.prototype.fillRect"),
    CANVASRENDERINGCONTEXT2D_STROKE_RECT("CanvasRenderingContext2D.prototype.strokeRect"),

    // Paths
    CANVASRENDERINGCONTEXT2D_BEGIN_PATH("CanvasRenderingContext2D.prototype.beginPath"),
    CANVASRENDERINGCONTEXT2D_CLOSE_PATH("CanvasRenderingContext2D.prototype.closePath"),
    CANVASRENDERINGCONTEXT2D_MOVE_TO("CanvasRenderingContext2D.prototype.moveTo"),
    CANVASRENDERINGCONTEXT2D_LINE_TO("CanvasRenderingContext2D.prototype.lineTo"),
    CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO("CanvasRenderingContext2D.prototype.quadraticCurveTo"),
    CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO("CanvasRenderingContext2D.prototype.bezierCurveTo"),
    CANVASRENDERINGCONTEXT2D_ARC_TO("CanvasRenderingContext2D.prototype.arcTo"),
    CANVASRENDERINGCONTEXT2D_RECT("CanvasRenderingContext2D.prototype.rect"),
    CANVASRENDERINGCONTEXT2D_ARC("CanvasRenderingContext2D.prototype.arc"),
    CANVASRENDERINGCONTEXT2D_FILL("CanvasRenderingContext2D.prototype.fill"),
    CANVASRENDERINGCONTEXT2D_STROKE("CanvasRenderingContext2D.prototype.stroke"),
    CANVASRENDERINGCONTEXT2D_CLIP("CanvasRenderingContext2D.prototype.clip"),
    CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH("CanvasRenderingContext2D.prototype.isPointInPath"),

    // Focus Management
    CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING("CanvasRenderingContext2D.prototype.drawFocusRing"),
    CANVASRENDERINGCONTEXT2D_FILL_TEXT("CanvasRenderingContext2D.prototype.fillText"),
    CANVASRENDERINGCONTEXT2D_STROKE_TEXT("CanvasRenderingContext2D.prototype.strokeText"),
    CANVASRENDERINGCONTEXT2D_MEASURE_TEXT("CanvasRenderingContext2D.prototype.measureText"),

    // Drawing Images
    CANVASRENDERINGCONTEXT2D_DRAW_IMAGE("CanvasRenderingContext2D.prototype.drawImage"),

    // Pixel Manipulation
    CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA("CanvasRenderingContext2D.prototype.createImageData"),
    CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA("CanvasRenderingContext2D.prototype.getImageData"),
    CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA("CanvasRenderingContext2D.prototype.putImageData"),

    CANVASGRADIENT("CanvasGradient"),
    CANVASGRADIENT_ADD_COLOR_STOP("CanvasGradient.addColorStop"),

    CANVASPATTERN("CanvasPattern"),
    CANVASPIXELARRAY("CanvasPixelArray"),
    IMAGEDATA("ImageData"),
    TEXTMETRICS("TextMetrics"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 WebGL ///
    // /////////////////////////////////////////////////////////////////////////
    WEBGLRENDERINGCONTEXT_CONSTRCUTOR("WebGLRenderingContext.constructor"),
    WEBGLRENDERINGCONTEXT_PROTOTYPE("WebGLRenderingContext.prototype"),
    WEBGLRENDERINGCONTEXT_INSTANCES("WebGLRenderingContext.instances"),
    WEBGLRENDERINGCONTEXT_TAJS_UNSUPPORTED_FUNCTION("WebGLRenderingContext.prototype.TAJS_UNSUPPORTED_FUNCTION"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 Media ///
    // /////////////////////////////////////////////////////////////////////////
    HTMLMEDIAELEMENT_CONSTRUCTOR("HTMLMediaElement"),
    HTMLMEDIAELEMENT_PROTOTYPE("HTMLMediaElement.prototype"),
    HTMLMEDIAELEMENT_INSTANCES("HTMLMediaElement instances"),
    HTMLMEDIAELEMENT_CAN_PLAY_TYPE("HTMLMediaElement.prototype.canPlayType"),
    HTMLMEDIAELEMENT_FAST_SEEK("HTMLMediaElement.prototype.fastSeek"),
    HTMLMEDIAELEMENT_LOAD("HTMLMediaElement.prototype.load"),
    HTMLMEDIAELEMENT_PLAY("HTMLMediaElement.prototype.play"),
    HTMLMEDIAELEMENT_PAUSE("HTMLMediaElement.prototype.pause"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 Audio ///
    // /////////////////////////////////////////////////////////////////////////
    HTMLAUDIOELEMENT_CONSTRUCTOR("HTMLAudioElement"),
    HTMLAUDIOELEMENT_PROTOTYPE("HTMLAudioElement.prototype"),
    HTMLAUDIOELEMENT_INSTANCES("HTMLAudioElement.instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 AudioContext ///
    // /////////////////////////////////////////////////////////////////////////
    AUDIOCONTEXT_CONSTRUCTOR("AudioContext"),
    AUDIOCONTEXT_PROTOTYPE("AudioContext.prototype"),
    AUDIOCONTEXT_INSTANCES("AudioContext instances"),
    AUDIOCONTEXT_CREATE_ANALYSER("AudioContext.createAnalyser"),
    AUDIOCONTEXT_CREATE_OSCILLATOR("AudioContext.createOscillator"),
    AUDIOCONTEXT_CREATE_SCRIPT_PROCESSOR("AudioContext.createScriptProcessor"),
    AUDIOCONTEXT_TAJS_UNSUPPORTED_FUNCTION("AudioContext.prototype.TAJS_UNSUPPORTED_FUNCTION"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 AudioNode ///
    // /////////////////////////////////////////////////////////////////////////
    AUDIONODE_CONSTRUCTOR("AudioNode"),
    AUDIONODE_PROTOTYPE("AudioNode.prototype"),
    AUDIONODE_INSTANCES("AudioNode.instances"),
    AUDIONODE_CONNECT("AudioNode.prototype.connect"),
    AUDIONODE_DISCONNECT("AudioNode.prototype.disconnect"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 OscillatorNode ///
    // /////////////////////////////////////////////////////////////////////////
    OSCILLATORNODE_CONSTRUCTOR("OscillatorNode"),
    OSCILLATORNODE_PROTOTYPE("OscillatorNode.prototype"),
    OSCILLATORNODE_INSTANCES("OscillatorNode instances"),
    OSCILLATORNODE_START("OscillatorNode.prototype.start"),
    OSCILLATORNODE_STOP("OscillatorNode.prototype.stop"),
    OSCILLATORNODE_SET_PERIODIC_WAVE("OscillatorNode.prototype.setPeriodicWave"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 AudioParam ///
    // /////////////////////////////////////////////////////////////////////////
    AUDIOPARAM_CONSTRUCTOR("AudioParam"),
    AUDIOPARAM_PROTOTYPE("AudioParam.prototype"),
    AUDIOPARAM_INSTANCES("AudioParam.instances"),
    AUDIOPARAM_TAJS_UNSUPPORTED_FUNCTION("AudioParam.prototype.TAJS_UNSUPPORTED_FUNCTION"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 ScriptProcessorNode ///
    // /////////////////////////////////////////////////////////////////////////
    SCRIPTPROCESSORNODE_CONSTRUCTOR("ScriptProcessorNode"),
    SCRIPTPROCESSORNODE_PROTOTYPE("ScriptProcessorNode.prototype"),
    SCRIPTPROCESSORNODE_INSTANCES("ScriptProcessorNode.instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 AudioDestinationNdoe ///
    // /////////////////////////////////////////////////////////////////////////
    AUDIODESTINATIONNODE_CONSTRUCTOR("AudioDestinationNode"),
    AUDIODESTINATIONNODE_PROTOTYPE("AudioDestinationNode.prototype"),
    AUDIODESTINATIONNODE_INSTANCES("AudioDestinationNode.instances"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 TimeRanges (used in Media) ///
    // /////////////////////////////////////////////////////////////////////////
    TIMERANGES_CONSTRUCTOR("TimeRanges"),
    TIMERANGES_PROTOTYPE("TimeRanges.prototype"),
    TIMERANGES_INSTANCES("TimeRanges instances"),
    TIMERANGES_START("TimeRanges.prototype.start"),
    TIMERANGES_END("TimeRanges.prototype.end"),

    // /////////////////////////////////////////////////////////////////////////
    // / HTML5 Storage///
    // /////////////////////////////////////////////////////////////////////////
    STORAGE_CONSTRUCTOR("Storage constructor"),
    STORAGE_PROTOTYPE("Storage.prototype"),
    STORAGE_INSTANCES("Storage instances"),
    STORAGE_KEY("Storage.prototype.key"),
    STORAGE_GET_ITEM("Storage.prototype.getItem"),
    STORAGE_SET_ITEM("Storage.prototype.setItem"),
    STORAGE_REMOVE_ITEM("Storage.prototype.removeItem"),

    // /////////////////////////////////////////////////////////////////////////
    // / AJAX ///
    // /////////////////////////////////////////////////////////////////////////
    READY_STATE_EVENT_PROTOTYPE("ReadystateEvent.prototype"),
    READY_STATE_EVENT_INSTANCES("ReadystateEvent instances"),
    XML_HTTP_REQUEST_INSTANCES("XmlHttpRequest"),
    XML_HTTP_REQUEST_CONSTRUCTOR("XmlHttpRequest"),
    XML_HTTP_REQUEST_OPEN("XmlHttpRequest.open"),
    XML_HTTP_REQUEST_SEND("XmlHttpRequest.send"),
    XML_HTTP_REQUEST_SET_REQUEST_HEADER("XmlHttpRequest.setRequestHeader"),
    XML_HTTP_REQUEST_ABORT("XmlHttpRequest.abort"),
    XML_HTTP_REQUEST_GET_RESPONSE_HEADER("XmlHttpRequest.getResponseHeader"),
    XML_HTTP_REQUEST_GET_ALL_RESPONSE_HEADERS("XmlHttpRequest.getAllResponseHeaders"),
    XML_HTTP_REQUEST_PROTOTYPE("XmlHttpRequest.prototype"),
    ACTIVE_X_OBJECT_INSTANCES("ActiveXObject"),
    ACTIVE_X_OBJECT_PROTOTYPE("ActiveXObject.prototype"),
    ACTIVE_X_OBJECT_CONSTRUCTOR("ActiveXObject"),
    ACTIVE_X_OBJECT_OPEN("ActiveXObject.open"),
    ACTIVE_X_OBJECT_SEND("ActiveXObject.send"),
    ACTIVE_X_OBJECT_SET_REQUEST_HEADER("ActiveXObject.setRequestHeader"),
    ACTIVE_X_OBJECT_ABORT("ActiveXObject.abort"),
    ACTIVE_X_OBJECT_GET_RESPONSE_HEADER("ActiveXObject.getResponseHeader"),
    ACTIVE_X_OBJECT_GET_ALL_RESPONSE_HEADERS("ActiveXObject.getAllResponseHeaders"),
    JSON_OBJECT("JSONObject");

    private HostAPIs api;

    private String string;

    DOMObjects(String str) {
        api = HostAPIs.DOCUMENT_OBJECT_MODEL;
        string = str;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public HostAPIs getAPI() {
        return api;
    }

    @Override
    public void evaluateSetter(ObjectLabel objlabel, Str prop, Value value, State state) {
        evaluateDOMSetter(objlabel, prop, value, state);
    }

    public static void evaluateDOMSetter(ObjectLabel objlabel, Str prop, Value value, State state) {
        if (objlabel.getHostObject().getAPI() != HostAPIs.DOCUMENT_OBJECT_MODEL && objlabel.getHostObject().getAPI() != HostAPIs.ECMASCRIPT_NATIVE) {
            state.setToNone();
            return;
        }
        if (prop.isMaybeSingleStr()) {
            DOMEvents.addEventHandler(Collections.singleton(objlabel), state, prop.getStr(), value, true);
            // TODO: other DOM setters to consider?
        } // FIXME: currently ignoring unknown property assignments in DOM setters
    }
}
