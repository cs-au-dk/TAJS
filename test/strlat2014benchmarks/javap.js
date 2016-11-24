// HTML 5 stub
var __AnyNum__ = Math.random();
DataView = function() {};
DataView.prototype.getInt8 = function() {
    return __AnyNum__;
};
DataView.prototype.getUint8 = function() {
    return __AnyNum__;
};
DataView.prototype.getInt16 = function() {
    return __AnyNum__;
};
DataView.prototype.getUint16 = function() {
    return __AnyNum__;
};
DataView.prototype.getInt32 = function() {
    return __AnyNum__;
};
DataView.prototype.getUint32 = function() {
    return __AnyNum__;
};
DataView.prototype.getFloat32 = function() {
    return __AnyNum__;
};
DataView.prototype.getFloat64 = function() {
    return __AnyNum__;
};



if (typeof JVM === 'undefined') {
    JVM = {};
}

(function () {

    JVM.ClassLoader = function () {

    };


    JVM.ClassLoader.prototype = {
        loadClass: function (binary) {
            var classFileParser = ClassFileParser(binary);
            var klass = classFileParser.parse();

            return klass;
        }
    };


    function ClassFileParser(binary) {
        var binary = new DataView(binary),
            offset = 0,
            klass = {};

        return {
            parse: function () {
                if (!isClassFile()) {
                    throw new Error('LinkageError');
                }

                readMinorVersion();
                readMajorVersion();
                readConstantPoolCount();
                readConstantPool();
                readAccessFlags();
                readThisClass();
                readSuperClass();
                readInterfacesCount();
                readInterfaces();
                readFieldsCount();
                readFields();
                readMethodsCount();
                readMethods();
                readAttributesCount();
                readAttributes();

                return klass;
            }
        };

        function getU1() {
            var byte_ = binary.getInt8(offset) & 0x000000FF;
            offset += 1;
            return byte_;
        }

        function getU2() {
            var bytes = binary.getUint16(offset, false) & 0x0000FFFF;
            offset += 2;
            return bytes;
        }

        function getU4() {
            var bytes = binary.getUint32(offset, false);
            offset += 4;
            return bytes;
        }

        function getShort() {
            offset += 2;
            return binary.getInt16(offset, false);
        }

        function getInt() {
            offset += 4;
            return binary.getInt32(offset, false);
        }

        function getLong() {
            offset += 8;
            return binary.getFloat64(offset, false);
        }

        function getFloat() {
            offset += 4;
            return binary.getFloat32(offset, false);
        }

        function getDouble() {
            offset += 8;
            return binary.getFloat64(offset, false);
        }

        function isClassFile() {
            klass.magic = getU4();
            return klass.magic === 0xCAFEBABE;
        }

        function readMinorVersion() {
            klass.minorVersion = getU2();
        }

        function readMajorVersion() {
            klass.majorVersion = getU2();
        }

        function readConstantPoolCount() {
            klass.constantPoolCount = getU2();
        }

        function readConstantPool() {
            var count = klass.constantPoolCount - 1;
            klass.constantPool = [
                {} // 0th constantPool is reserved by JVM
            ];
            var info;
            for (var i = 0; i < count; i++) {
                info = {};
                getCPInfo(info);
                klass.constantPool.push(info);
                if (info.tag === 5 || info.tag === 6) {
                    klass.constantPool.push({});
                    i++;
                }
            }

        }

        function readAccessFlags() {
            klass.accessFlags = getU2();
        }

        function readThisClass() {
            klass.thisClass = getU2();
        }

        function readSuperClass() {
            klass.superClass = getU2();
        }

        function readInterfacesCount() {
            klass.interfacesCount = getU2();
        }

        function readInterfaces() {
            var count = klass.interfacesCount;
            var interfaces = [];
            for (var i = 0; i < count; i++) {
                interfaces.push(getU2());
            }
            klass.interfaces = interfaces;
        }

        function readFieldsCount() {
            klass.fieldsCount = getU2();
        }

        function readFields() {
            var count = klass.fieldsCount;
            var fields = [];
            for (var i = 0; i < count; i++) {
                fields.push(getFieldInfo());
            }
            klass.fields = fields;
        }

        function readMethodsCount() {
            klass.methodsCount = getU2();
        }

        function readMethods() {
            var count = klass.methodsCount;
            var methods = [];
            for (var i = 0; i < count; i++) {
                methods.push(getMethodInfo());
            }
            klass.methods = methods;
        }

        function readAttributesCount() {
            klass.attributesCount = getU2();
        }

        function readAttributes() {
            var count = klass.attributesCount;
            var attributes = [];
            for (var i = 0; i < count; i++) {
                attributes.push(getAttribute());
            }
            klass.attributes = attributes;
        }

        function getCPInfo(info) {
            info.tag = getU1();
            var fn;

            switch (info.tag) {
                case 1:
                    fn = getConstantUTF8;
                    break;
                case 3:
                    fn = getConstantInteger;
                    break;
                case 4:
                    fn = getConstantFloat;
                    break;
                case 5:
                    fn = getConstantLong;
                    break;
                case 6:
                    fn = getConstantDouble;
                    break;
                case 7:
                    fn = getConstantClass;
                    break;
                case 8:
                    fn = getConstantString;
                    break;
                case 9:
                    fn = getConstantFieldref;
                    break;
                case 10:
                    fn = getConstantMethodref;
                    break;
                case 11:
                    fn = getConstantInterfaceMethodref;
                    break;
                case 12:
                    fn = getConstantNameAndType;
                    break;
                case 15:
                    fn = getConstantMethodHandle;
                    break;
                case 16:
                    fn = getConstantMethodType;
                    break;
                case 18:
                    fn = getConstantInvokeDynamic;
                    break;
                default:
                    throw new Error('LinkageError');

            }

            fn(info);

        }

        function getConstantUTF8(info) {
            var length = getU2();
            var value = '';

            var i = 0;
            var c;
            for (; i < length; i++) {
                var b = getU1();

                if (b <= 0x7f) {
                    value += String.fromCharCode(b);
                } else if (b <= 0xdf) {
                    c = ((b & 0x1f) << 6);
                    i++;
                    c += getU1() & 0x3f;
                    value += String.fromCharCode(c);
                } else if (b <= 0xe0) {
                    i++;
                    c = ((getU1() & 0x1f) << 6) | 0x0800;
                    i++;
                    c += getU1() & 0x3f;
                    value += String.fromCharCode(c);
                } else {
                    c = ((b & 0x0f) << 12);
                    i++;
                    c += (getU1() & 0x3f) << 6;
                    i++;
                    c += getU1() & 0x3f;
                    value += String.fromCharCode(c);
                }
            }
            info.length = length;
            info.bytes = value;

        }

        function getConstantInteger(info) {
            info.bytes = getU4();

        }

        function getConstantFloat(info) {
            info.bytes = getU4();

        }

        function getConstantLong(info) {
            info.highBytes = getU4();
            info.lowBytes = getU4();

        }

        function getConstantDouble(info) {
            info.highBytes = getU4();
            info.lowBytes = getU4();

        }

        function getConstantClass(info) {
            info.nameIndex = getU2();

        }

        function getConstantString(info) {
            info.stringIndex = getU2();

        }

        function getConstantFieldref(info) {
            info.classIndex = getU2();
            info.nameTypeIndex = getU2();

        }

        function getConstantMethodref(info) {
            info.classIndex = getU2();
            info.nameTypeIndex = getU2();

        }

        function getConstantInterfaceMethodref(info) {
            info.classIndex = getU2();
            info.nameTypeIndex = getU2();

        }

        function getConstantNameAndType(info) {
            info.nameIndex = getU2();
            info.descriptorIndex = getU2();

        }

        function getConstantMethodHandle(info) {
            info.referenceKind = getU1();
            info.referenceIndex = getU2();
        }

        function getConstantMethodType(info) {
            info.descriptorIndex = getU2();
        }

        function getConstantInvokeDynamic(info) {
            info.bootstrapMethodAttrIndex = getU2();
            info.nameAndTypeIndex = getU2();
        }

        function getFieldInfo() {
            var info = {};
            info.accessFlags = getU2();
            info.nameIndex = getU2();
            info.descriptorIndex = getU2();
            info.attributeCount = getU2();

            var count = info.attributeCount;
            var attributes = [];

            for (var i = 0; i < count; i++) {
                attributes.push(getAttribute());
            }

            info.attributes = attributes;

            return info;
        }

        function getAttribute() {
            var attr = {};
            attr.attributeNameIndex = getU2();
            var attributeName = klass.constantPool[attr.attributeNameIndex];

            var fn;
            switch (attributeName.bytes) {
                case 'ConstantValue':
                    fn = getConstantValue;
                    break;

                case 'Code':
                    fn = getCode;
                    break;

                case 'StackMapTable':
                    fn = getStackMapTable;
                    break;

                case 'Exceptions':
                    fn = getExceptions;
                    break;

                case 'InnerClasses':
                    fn = getInnerClasses;
                    break;

                case 'EnclosingMethod':
                    fn = getEnclosingMethod;
                    break;

                case 'Synthetic':
                    fn = getSynthetic;
                    break;

                case 'Signature':
                    fn = getSignature;
                    break;

                case 'SourceFile':
                    fn = getSourceFile;
                    break;

                case 'SourceDebugExtension':
                    fn = getSourceDebugExtension;
                    break;

                case 'LineNumberTable':
                    fn = getLineNumberTable;
                    break;

                case 'LocalVariableTable':
                    fn = getLocalVariableTable;
                    break;

                case 'LocalVariableTypeTable':
                    fn = getLocalVariableTypeTable;
                    break;

                case 'Deprecated':
                    fn = getDeprecated;
                    break;

                case 'RuntimeVisibleAnnotations':
                    fn = getRuntimeVisibleAnnotations;
                    break;

                case 'RuntimeInvisibleAnnotations':
                    fn = getRuntimeInvisibleAnnotations;
                    break;

                case 'RuntimeVisibleParameterAnnotations':
                    fn = getRuntimeVisibleParameterAnnotations;
                    break;

                case 'RuntimeInvisibleParameterAnnotations':
                    fn = getRuntimeInvisibleParameterAnnotations;
                    break;

                case 'AnnotationDefault':
                    fn = getAnnotationDefault;
                    break;

                case 'BootstrapMethods':
                    fn = getBootstrapMethods;
                    break;

                case 'MethodParameters':
                    fn = getMethodParameters;
                    break

                default :
                    throw new Error('LinkageError');
            }

            fn(attr);
            return attr;

        }

        function getConstantValue(attr) {
            attr.attributeLength = getU4();
            attr.constantValueIndex = getU2();

        }

        function getCode(attr) {
            var code = [],
                exceptionTable = [],
                attributes = [];


            attr.attributeLength = getU4();
            attr.maxStack = getU2();
            attr.maxLocals = getU2();
            attr.codeLength = getU4();


            var i, len = attr.codeLength;
            for (i = 0; i < len; i++) {
                var b = getU1();
                code.push(b);
            }
            attr.code = JVM.ByteCodeParser.parse(code);

            attr.exceptionTableLength = getU2();

            len = attr.exceptionTableLength;
            for (i = 0; i < len; i++) {
                exceptionTable.push(getExceptionTable());
            }

            attr.exceptionTable = exceptionTable;
            attr.attributesCount = getU2();


            len = attr.attributesCount;
            for (i = 0; i < len; i++) {
                attributes.push(getAttribute());
            }

            attr.attributes = attributes;

        }

        function getExceptionTable() {
            var exceptionTable = {};
            exceptionTable.startPC = getU2();
            exceptionTable.endPC = getU2();
            exceptionTable.handlerPC = getU2();
            exceptionTable.catchType = getU2();

            return exceptionTable;
        }

        function getStackMapTable(attr) {
            var i,
                len,
                entries = [];

            attr.attributeLength = getU4();
            attr.numberOfEntries = getU2();

            len = attr.numberOfEntries;
            for (i = 0; i < len; i++) {
                entries.push(getStackMapFrame());
            }

            attr.entries = entries;

        }

        function getStackMapFrame() {
            var frame = {};

            frame.frameType = getU1();

            if (frame.frameType < 64) {
                //same_frame
            } else if (frame.frameType < 128) {
                //same_locals_1_stack_item_frame
                frame.stack = getVerificationTypeInfo();
            } else if (frame.frameType === 247) {
                //same_locals_1_stack_item_frame_extended
                frame.offsetDelta = getU2();
                frame.stack = getVerificationTypeInfo();
            } else if (248 <= frame.frameType && frame.frameType <= 250) {
                //chop_frame
                frame.offsetDelta = getU2();
            } else if (frame.frameType === 251) {
                //same_frame_extended
                frame.offsetDelta = getU2();
            } else if (252 <= frame.frameType && frame.frameType <= 254) {
                //append_frame
                getAppendFrame(frame);
            } else if (frame.frameType === 255) {
                //full_frame
                getFullFrame(frame);
            } else {
                throw new Error('LinkageError');
            }

            return frame;

        }


        function getAppendFrame(frame) {
            var i, len = frame.frameType - 251;
            frame.offsetDelta = getU2();
            frame.locals = [];

            for (i = 0; i < len; i++) {
                frame.locals.push(getVerificationTypeInfo());
            }

        }

        function getFullFrame(frame) {
            var i, len;
            frame.offsetDelta = getU2();
            frame.numberOfLocals = getU2();
            frame.locals = [];

            len = frame.numberOfLocals;
            for (i = 0; i < len; i++) {
                frame.locals.push(getVerificationTypeInfo());
            }

            len = frame.numberOfStackItems = getU2();
            frame.stack = [];
            for (i = 0; i < len; i++) {
                frame.stack.push(getVerificationTypeInfo());
            }
        }

//      verification_type_info

        function getVerificationTypeInfo() {
            var verificationTypeInfo = {};
            verificationTypeInfo.tag = getU1();

            if (verificationTypeInfo.tag === 0) {
                //Top_variable_info
            } else if (verificationTypeInfo.tag === 1) {
                //Integer_variable_info
            } else if (verificationTypeInfo.tag === 2) {
                //Float_variable_info
            } else if (verificationTypeInfo.tag === 3) {
                //Double_variable_info
            } else if (verificationTypeInfo.tag === 4) {
                //Long_variable_info
            } else if (verificationTypeInfo.tag === 5) {
                //Null_variable_info
            } else if (verificationTypeInfo.tag === 6) {
                //UninitializedThis_variable_info
            } else if (verificationTypeInfo.tag === 7) {
                //Object_variable_info
                verificationTypeInfo.cpoolIndex = getU2();
            } else if (verificationTypeInfo.tag === 8) {
                //Uninitialized_variable_info
                verificationTypeInfo.offset = getU2();
            } else {
                throw new Error('LinkageError');
            }
        }


        function getExceptions(attr) {
            var i,
                len,
                exceptionIndexTable = [];

            attr.attributeLength = getU4();
            attr.numberOfExceptions = getU2();

            len = attr.numberOfExceptions;
            for (i = 0; i < len; i++) {
                exceptionIndexTable.push(getU2());
            }

            attr.exceptionIndexTable = exceptionIndexTable;
        }

        function getInnerClasses(attr) {
            var i,
                len,
                classes = [];

            attr.attributeLength = getU4();
            attr.numberOfClasses = getU2();

            len = attr.numberOfClasses;
            for (i = 0; i < len; i++) {
                classes.push(getClasses());
            }
            attr.classes = classes;
        }

        function getClasses() {
            var classes = {};

            classes.innerClassInfoIndex = getU2();
            classes.outerClassInfoIndex = getU2();
            classes.innerNameIndex = getU2();
            classes.innerClassAccessFlags = getU2();

            return classes;
        }

        function getEnclosingMethod(attr) {
            attr.attributeLength = getU4();
            attr.classIndex = getU2();
            attr.methodIndex = getU2();
        }

        function getSynthetic(attr) {
            attr.attributeLength = getU4();

        }

        function getSignature(attr) {
            attr.attributeLength = getU4();
            attr.signatureIndex = getU2();

        }

        function getSourceFile(attr) {
            attr.attributeLength = getU4();
            attr.sourcefileIndex = getU2();
        }

        function getSourceDebugExtension(attr) {
            var i,
                len,
                debugExtension = [];

            len = attr.attributeLength = getU4();
            for (i = 0; i < len; i++) {
                debugExtension.push(getU1());
            }

        }


        function getLineNumberTable(attr) {
            var i,
                len,
                lineNumberTable = [],
                table;

            attr.attributeLength = getU4();
            attr.lineNumberTableLength = getU2();

            len = attr.lineNumberTableLength;
            for (i = 0; i < len; i++) {
                table = {};
                table.startPC = getU2();
                table.lineNumber = getU2();
                lineNumberTable.push(table);
            }

            attr.lineNumberTable = lineNumberTable;
        }

        function getLocalVariableTable(attr) {
            var i,
                len,
                localVariableTable = [],
                table;

            attr.attributeLength = getU4();
            attr.localVariableTableLength = getU2();

            len = attr.localVariableTableLength;
            for (i = 0; i < len; i++) {
                table = {};
                table.startPC = getU2();
                table.length = getU2();
                table.nameIndex = getU2();
                table.descriptorIndex = getU2();
                table.index = getU2();
                localVariableTable.push(table);
            }

            attr.localVariableTable = localVariableTable;
        }

        function getLocalVariableTypeTable(attr) {
            var i,
                len,
                localVariableTypeTable = [],
                table;

            attr.attributeLength = getU4();
            attr.localVariableTypeTableLength = getU2();
            len = attr.localVariableTypeTableLength;
            for (i = 0; i < len; i++) {
                table = {};
                table.startPC = getU2();
                table.length = getU2();
                table.nameIndex = getU2();
                table.signatureIndex = getU2();
                table.index = getU2();
                localVariableTypeTable.push(table);
            }

            attr.localVariableTypeTable = localVariableTypeTable;
        }

        function getDeprecated(attr) {
            attr.attributeLength = getU4();

        }

        function getRuntimeVisibleAnnotations(attr) {
            attr.attributeLength = getU4();
            attr.numAnnotations = getU2();
            attr.annotations = getAnnotations(attr);

        }

        function getRuntimeInvisibleAnnotations(attr) {
            attr.attributeLength = getU4();
            attr.numAnnotations = getU2();
            attr.annotations = getAnnotations(attr);

        }

        function getRuntimeVisibleParameterAnnotations(attr) {
            var i,
                j,
                len,
                jLen,
                parameterAnnotations = [],
                annotation;

            attr.attributeLength = getU4();
            len = attr.numParameters = getU1();
            for (i = 0; i < len; i++) {
                annotation = {};
                annotation.numAnnotations = getU2();
                jLen = annotation.numAnnotations;
                annotation.annotations = [];
                for (j = 0; j < jLen; j++) {
                    annotation.annotations.push(getAnnotation());
                }
                parameterAnnotations.push(annotation);
            }

            attr.parameterAnnotations = parameterAnnotations;

        }

        function getRuntimeInvisibleParameterAnnotations(attr) {
            var i,
                j,
                len,
                jLen,
                parameterAnnotations = [],
                annotation;

            attr.attributeLength = getU4();
            len = attr.numParameters = getU1();
            for (i = 0; i < len; i++) {
                annotation = {};
                annotation.numAnnotations = getU2();
                jLen = annotation.numAnnotations;
                annotation.annotations = [];
                for (j = 0; j < jLen; j++) {
                    annotation.annotations.push(getAnnotation());
                }
                parameterAnnotations.push(annotation);
            }

            attr.parameterAnnotations = parameterAnnotations;

        }

        function getAnnotationDefault(attr) {
            attr.attributeLength = getU4();
            attr.defaultValue = getElementValue();
        }

        function getAnnotations(attr) {
            var annotations = [],
                i = 0,
                len = attr.numAnnotations;

            for (; i < len; i++) {

                annotations.push(getAnnotation());
            }

            return annotations;

        }

        function getAnnotation() {
            var annotation = {};
            annotation.typeIndex = getU2();
            annotation.numElementValuePairs = getU2();
            annotation.elementValuePairs = [];

            var i = 0, len = annotation.numElementValuePairs;
            for (; i < len; i++) {
                annotation.elementValuePairs.push(getElementValuePair());
            }
            return annotation;
        }

        function getElementValuePair() {
            var elementValuePair = {};
            elementValuePair.elementNameIndex = getU2();
            elementValuePair.value = getElementValue();
            return elementValuePair;
        }


        function getElementValue() {
            var elementValue = {};
            elementValue.tag = getU1();

            switch (elementValue.tag) {
                case 0x42: // B byte
                case 0x43: // C char
                case 0x44: // D double
                case 0x46: // F float
                case 0x49: // I int
                case 0x4a: // J long
                case 0x53: // S short
                case 0x5a: // Z boolean
                case 0x73: // s String
                    elementValue.constValueIndex = getU2();
                    break;

                case 0x65: // e enum
                    elementValue.enumConstValue = {};
                    elementValue.enumConstValue.typeNameIndex = getU2();
                    elementValue.enumConstValue.constNameIndex = getU2();
                    break;

                case 0x63: // c class
                    elementValue.classInfoIndex = getU2();
                    break;

                case 0x40: // @ annotation
                    elementValue.annotationValue = getAnnotation();
                    break;

                case 0x5b: // [ array
                    elementValue.arrayValue = {};
                    elementValue.arrayValue.numValues = getU2();
                    elementValue.arrayValue.values = [];

                    var i = 0, len = elementValue.arrayValue.numValues;
                    for (; i < len; i++) {
                        elementValue.arrayValue.values.push(getElementValue());
                    }
                    break;

                default:
                    throw new Error('LinkageError');


            }
        }

        function getBootstrapMethods(attr) {
            var i,
                len,
                bootstrapMethods = [];

            attr.attributeLength = getU4();
            len = attr.numBootstrapMethods = getU2();
            for (i = 0; i < len; i++) {
                bootstrapMethods.push(getBootstrapMethod());
            }

            attr.bootstrapMethods = bootstrapMethods;
        }

        function getBootstrapMethod() {
            var i,
                len,
                bootstrapArguments = [],
                method = {};

            method.bootstrapMethodRef = getU2();
            len = method.numBootstrapArguments = getU2();

            for (i = 0; i < len; i++) {
                bootstrapArguments.push(getU2());
            }
            method.bootstrapArguments = bootstrapArguments;
            return method;
        }

        function getMethodParameters(attr) {
            var i,
                len,
                methodParameters = [],
                parameter;

            attr.attributeLength = getU4();
            len = attr.numMethodParameters = getU1();
            for (i = 0; i < len; i++) {
                parameter = {};
                parameter.nameIndex = getU2();
                parameter.flag = getU4();
                methodParameters.push(parameter)
            }

            attr.methodParameters = methodParameters;
        }

        function getMethodInfo() {
            var methodInfo = {};

            methodInfo.accessFlags = getU2();
            methodInfo.nameIndex = getU2();
            methodInfo.descriptorIndex = getU2();
            methodInfo.attributesCount = getU2();

            var count = methodInfo.attributesCount;
            var attributes = [];

            for (var i = 0; i < count; i++) {
                attributes.push(getAttribute());
            }

            methodInfo.attributes = attributes;

            return methodInfo;

        }

    }


    JVM.ByteCodeParser = {};


    var BYTECODE_DETAIL = [];
    BYTECODE_DETAIL[0] = bytecode('nop', 0);
    BYTECODE_DETAIL[1] = bytecode('aconst_null', 0);
    BYTECODE_DETAIL[2] = bytecode('iconst_m1', 0);
    BYTECODE_DETAIL[3] = bytecode('iconst_0', 0);
    BYTECODE_DETAIL[4] = bytecode('iconst_1', 0);
    BYTECODE_DETAIL[5] = bytecode('iconst_2', 0);
    BYTECODE_DETAIL[6] = bytecode('iconst_3', 0);
    BYTECODE_DETAIL[7] = bytecode('iconst_4', 0);
    BYTECODE_DETAIL[8] = bytecode('iconst_5', 0);
    BYTECODE_DETAIL[9] = bytecode('lconst_0', 0);
    BYTECODE_DETAIL[10] = bytecode('lconst_1', 0);
    BYTECODE_DETAIL[11] = bytecode('fconst_0', 0);
    BYTECODE_DETAIL[12] = bytecode('fconst_1', 0);
    BYTECODE_DETAIL[13] = bytecode('fconst_2', 0);
    BYTECODE_DETAIL[14] = bytecode('dconst_0', 0);
    BYTECODE_DETAIL[15] = bytecode('dconst_1', 0);
    BYTECODE_DETAIL[16] = bytecode('bipush', 1);
    BYTECODE_DETAIL[17] = bytecode('sipush', 2);
    BYTECODE_DETAIL[18] = bytecode('ldc', 1);
    BYTECODE_DETAIL[19] = bytecode('ldc_w', 2);
    BYTECODE_DETAIL[20] = bytecode('ldc2_w', 2);
    BYTECODE_DETAIL[21] = bytecode('iload', 1);
    BYTECODE_DETAIL[22] = bytecode('lload', 1);
    BYTECODE_DETAIL[23] = bytecode('fload', 1);
    BYTECODE_DETAIL[24] = bytecode('dload', 1);
    BYTECODE_DETAIL[25] = bytecode('aload', 1);
    BYTECODE_DETAIL[26] = bytecode('iload_0', 0);
    BYTECODE_DETAIL[27] = bytecode('iload_1', 0);
    BYTECODE_DETAIL[28] = bytecode('iload_2', 0);
    BYTECODE_DETAIL[29] = bytecode('iload_3', 0);
    BYTECODE_DETAIL[30] = bytecode('lload_0', 0);
    BYTECODE_DETAIL[31] = bytecode('lload_1', 0);
    BYTECODE_DETAIL[32] = bytecode('lload_2', 0);
    BYTECODE_DETAIL[33] = bytecode('lload_3', 0);
    BYTECODE_DETAIL[34] = bytecode('fload_0', 0);
    BYTECODE_DETAIL[35] = bytecode('fload_1', 0);
    BYTECODE_DETAIL[36] = bytecode('fload_2', 0);
    BYTECODE_DETAIL[37] = bytecode('fload_3', 0);
    BYTECODE_DETAIL[38] = bytecode('dload_0', 0);
    BYTECODE_DETAIL[39] = bytecode('dload_1', 0);
    BYTECODE_DETAIL[40] = bytecode('dload_2', 0);
    BYTECODE_DETAIL[41] = bytecode('dload_3', 0);
    BYTECODE_DETAIL[42] = bytecode('aload_0', 0);
    BYTECODE_DETAIL[43] = bytecode('aload_1', 0);
    BYTECODE_DETAIL[44] = bytecode('aload_2', 0);
    BYTECODE_DETAIL[45] = bytecode('aload_3', 0);
    BYTECODE_DETAIL[46] = bytecode('iaload', 0);
    BYTECODE_DETAIL[47] = bytecode('laload', 0);
    BYTECODE_DETAIL[48] = bytecode('faload', 0);
    BYTECODE_DETAIL[49] = bytecode('daload', 0);
    BYTECODE_DETAIL[50] = bytecode('aaload', 0);
    BYTECODE_DETAIL[51] = bytecode('baload', 0);
    BYTECODE_DETAIL[52] = bytecode('caload', 0);
    BYTECODE_DETAIL[53] = bytecode('saload', 0);
    BYTECODE_DETAIL[54] = bytecode('istore', 1);
    BYTECODE_DETAIL[55] = bytecode('lstore', 1);
    BYTECODE_DETAIL[56] = bytecode('fstore', 1);
    BYTECODE_DETAIL[57] = bytecode('dstore', 1);
    BYTECODE_DETAIL[58] = bytecode('astore', 1);
    BYTECODE_DETAIL[59] = bytecode('istore_0', 0);
    BYTECODE_DETAIL[60] = bytecode('istore_1', 0);
    BYTECODE_DETAIL[61] = bytecode('istore_2', 0);
    BYTECODE_DETAIL[62] = bytecode('istore_3', 0);
    BYTECODE_DETAIL[63] = bytecode('lstore_0', 0);
    BYTECODE_DETAIL[64] = bytecode('lstore_1', 0);
    BYTECODE_DETAIL[65] = bytecode('lstore_2', 0);
    BYTECODE_DETAIL[66] = bytecode('lstore_3', 0);
    BYTECODE_DETAIL[67] = bytecode('fstore_0', 0);
    BYTECODE_DETAIL[68] = bytecode('fstore_1', 0);
    BYTECODE_DETAIL[69] = bytecode('fstore_2', 0);
    BYTECODE_DETAIL[70] = bytecode('fstore_3', 0);
    BYTECODE_DETAIL[71] = bytecode('dstore_0', 0);
    BYTECODE_DETAIL[72] = bytecode('dstore_1', 0);
    BYTECODE_DETAIL[73] = bytecode('dstore_2', 0);
    BYTECODE_DETAIL[74] = bytecode('dstore_3', 0);
    BYTECODE_DETAIL[75] = bytecode('astore_0', 0);
    BYTECODE_DETAIL[76] = bytecode('astore_1', 0);
    BYTECODE_DETAIL[77] = bytecode('astore_2', 0);
    BYTECODE_DETAIL[78] = bytecode('astore_3', 0);
    BYTECODE_DETAIL[79] = bytecode('iastore', 0);
    BYTECODE_DETAIL[80] = bytecode('lastore', 0);
    BYTECODE_DETAIL[81] = bytecode('fastore', 0);
    BYTECODE_DETAIL[82] = bytecode('dastore', 0);
    BYTECODE_DETAIL[83] = bytecode('aastore', 0);
    BYTECODE_DETAIL[84] = bytecode('bastore', 0);
    BYTECODE_DETAIL[85] = bytecode('castore', 0);
    BYTECODE_DETAIL[86] = bytecode('sastore', 0);
    BYTECODE_DETAIL[87] = bytecode('pop', 0);
    BYTECODE_DETAIL[88] = bytecode('pop2', 0);
    BYTECODE_DETAIL[89] = bytecode('dup', 0);
    BYTECODE_DETAIL[90] = bytecode('dup_x1', 0);
    BYTECODE_DETAIL[91] = bytecode('dup_x2', 0);
    BYTECODE_DETAIL[92] = bytecode('dup2', 0);
    BYTECODE_DETAIL[93] = bytecode('dup2_x1', 0);
    BYTECODE_DETAIL[94] = bytecode('dup2_x2', 0);
    BYTECODE_DETAIL[95] = bytecode('swap', 0);
    BYTECODE_DETAIL[96] = bytecode('iadd', 0);
    BYTECODE_DETAIL[97] = bytecode('ladd', 0);
    BYTECODE_DETAIL[98] = bytecode('fadd', 0);
    BYTECODE_DETAIL[99] = bytecode('dadd', 0);
    BYTECODE_DETAIL[100] = bytecode('isub', 0);
    BYTECODE_DETAIL[101] = bytecode('lsub', 0);
    BYTECODE_DETAIL[102] = bytecode('fsub', 0);
    BYTECODE_DETAIL[103] = bytecode('dsub', 0);
    BYTECODE_DETAIL[104] = bytecode('imul', 0);
    BYTECODE_DETAIL[105] = bytecode('lmul', 0);
    BYTECODE_DETAIL[106] = bytecode('fmul', 0);
    BYTECODE_DETAIL[107] = bytecode('dmul', 0);
    BYTECODE_DETAIL[108] = bytecode('idiv', 0);
    BYTECODE_DETAIL[109] = bytecode('ldiv', 0);
    BYTECODE_DETAIL[110] = bytecode('fdiv', 0);
    BYTECODE_DETAIL[111] = bytecode('ddiv', 0);
    BYTECODE_DETAIL[112] = bytecode('irem', 0);
    BYTECODE_DETAIL[113] = bytecode('lrem', 0);
    BYTECODE_DETAIL[114] = bytecode('frem', 0);
    BYTECODE_DETAIL[115] = bytecode('drem', 0);
    BYTECODE_DETAIL[116] = bytecode('ineg', 0);
    BYTECODE_DETAIL[117] = bytecode('lneg', 0);
    BYTECODE_DETAIL[118] = bytecode('fneg', 0);
    BYTECODE_DETAIL[119] = bytecode('dneg', 0);
    BYTECODE_DETAIL[120] = bytecode('ishl', 0);
    BYTECODE_DETAIL[121] = bytecode('lshl', 0);
    BYTECODE_DETAIL[122] = bytecode('ishr', 0);
    BYTECODE_DETAIL[123] = bytecode('lshr', 0);
    BYTECODE_DETAIL[124] = bytecode('iushr', 0);
    BYTECODE_DETAIL[125] = bytecode('lushr', 0);
    BYTECODE_DETAIL[126] = bytecode('iadn', 0);
    BYTECODE_DETAIL[127] = bytecode('land', 0);
    BYTECODE_DETAIL[128] = bytecode('ior', 0);
    BYTECODE_DETAIL[129] = bytecode('lor', 0);
    BYTECODE_DETAIL[130] = bytecode('ixor', 0);
    BYTECODE_DETAIL[131] = bytecode('lxor', 0);
    BYTECODE_DETAIL[132] = bytecode('iinc', 2);
    BYTECODE_DETAIL[133] = bytecode('i2l', 0);
    BYTECODE_DETAIL[134] = bytecode('i2f', 0);
    BYTECODE_DETAIL[135] = bytecode('i2d', 0);
    BYTECODE_DETAIL[136] = bytecode('l2i', 0);
    BYTECODE_DETAIL[137] = bytecode('l2f', 0);
    BYTECODE_DETAIL[138] = bytecode('l2d', 0);
    BYTECODE_DETAIL[139] = bytecode('f2i', 0);
    BYTECODE_DETAIL[140] = bytecode('f2l', 0);
    BYTECODE_DETAIL[141] = bytecode('f2d', 0);
    BYTECODE_DETAIL[142] = bytecode('d2i', 0);
    BYTECODE_DETAIL[143] = bytecode('d2l', 0);
    BYTECODE_DETAIL[144] = bytecode('d2f', 0);
    BYTECODE_DETAIL[145] = bytecode('i2b', 0);
    BYTECODE_DETAIL[146] = bytecode('i2c', 0);
    BYTECODE_DETAIL[147] = bytecode('i2s', 0);
    BYTECODE_DETAIL[148] = bytecode('lcmp', 0);
    BYTECODE_DETAIL[149] = bytecode('fcmpl', 0);
    BYTECODE_DETAIL[150] = bytecode('fcmpg', 0);
    BYTECODE_DETAIL[151] = bytecode('dcmpl', 0);
    BYTECODE_DETAIL[152] = bytecode('dcmpg', 0);
    BYTECODE_DETAIL[153] = bytecode('ifeq', 2);
    BYTECODE_DETAIL[154] = bytecode('ifne', 2);
    BYTECODE_DETAIL[155] = bytecode('iflt', 2);
    BYTECODE_DETAIL[156] = bytecode('ifge', 2);
    BYTECODE_DETAIL[157] = bytecode('ifgt', 2);
    BYTECODE_DETAIL[158] = bytecode('ifle', 2);
    BYTECODE_DETAIL[159] = bytecode('if_icmpeq', 2);
    BYTECODE_DETAIL[160] = bytecode('if_icmpne', 2);
    BYTECODE_DETAIL[161] = bytecode('if_icmplt', 2);
    BYTECODE_DETAIL[162] = bytecode('if_icmpge', 2);
    BYTECODE_DETAIL[163] = bytecode('if_icmpgt', 2);
    BYTECODE_DETAIL[164] = bytecode('if_icmple', 2);
    BYTECODE_DETAIL[165] = bytecode('if_acmpeq', 2);
    BYTECODE_DETAIL[166] = bytecode('if_acmpne', 2);
    BYTECODE_DETAIL[167] = bytecode('goto', 2);
    BYTECODE_DETAIL[168] = bytecode('jsr', 2);
    BYTECODE_DETAIL[169] = bytecode('ret', 1);
    BYTECODE_DETAIL[170] = bytecode('tableswitch'); //variable length instruction
    BYTECODE_DETAIL[171] = bytecode('lookupswitch'); // variable length instruction
    BYTECODE_DETAIL[172] = bytecode('ireturn', 0);
    BYTECODE_DETAIL[173] = bytecode('lreturn', 0);
    BYTECODE_DETAIL[174] = bytecode('freturn', 0);
    BYTECODE_DETAIL[175] = bytecode('dreturn', 0);
    BYTECODE_DETAIL[176] = bytecode('areturn', 0);
    BYTECODE_DETAIL[177] = bytecode('return', 0);
    BYTECODE_DETAIL[178] = bytecode('getstatic', 2);
    BYTECODE_DETAIL[179] = bytecode('putstatic', 2);
    BYTECODE_DETAIL[180] = bytecode('getfield', 2);
    BYTECODE_DETAIL[181] = bytecode('putfield', 2);
    BYTECODE_DETAIL[182] = bytecode('invokevirtual', 2);
    BYTECODE_DETAIL[183] = bytecode('involespecial', 2);
    BYTECODE_DETAIL[184] = bytecode('invokestatic', 2);
    BYTECODE_DETAIL[185] = bytecode('invokeinterface', 4);// 4th byte must be zero
    BYTECODE_DETAIL[186] = bytecode('invokedynamic', 4);
    BYTECODE_DETAIL[187] = bytecode('new', 2);
    BYTECODE_DETAIL[188] = bytecode('newarray', 1);
    BYTECODE_DETAIL[189] = bytecode('anewarray', 2);
    BYTECODE_DETAIL[190] = bytecode('arraylength', 0);
    BYTECODE_DETAIL[191] = bytecode('athrow', 0);
    BYTECODE_DETAIL[192] = bytecode('checkcast', 2);
    BYTECODE_DETAIL[193] = bytecode('instanceof', 2);
    BYTECODE_DETAIL[194] = bytecode('monitorenter', 0);
    BYTECODE_DETAIL[195] = bytecode('monitorexit', 0);
    BYTECODE_DETAIL[196] = bytecode('wide'); // variable length instruction
    BYTECODE_DETAIL[197] = bytecode('multianewarray', 3);
    BYTECODE_DETAIL[198] = bytecode('ifnull', 2);
    BYTECODE_DETAIL[199] = bytecode('ifnonnull', 2);
    BYTECODE_DETAIL[200] = bytecode('goto_w', 4);
    BYTECODE_DETAIL[201] = bytecode('jsr_w', 4);

    // reserved opcode
    BYTECODE_DETAIL[202] = bytecode('breakpoint');
    BYTECODE_DETAIL[254] = bytecode('impdep1');
    BYTECODE_DETAIL[255] = bytecode('impdep2');

    function bytecode(name, num) {
        var fn;

        if (name === 'tableswitch') {
            fn = parseTableswitch;
        } else if (name === 'lookupswitch') {
            fn = parseLookupswitch;
        } else if (name === 'wide') {
            fn = parseWide;
        } else if (num === 0) {
            fn = parse0;
        } else if (num === 1) {
            fn = parse1;
        } else if (num === 2) {
            fn = parse2;
        } else if (num === 3) {
            fn = parse3;
        } else if (num === 4) {
            fn = parse4;
        }
        return {
            name: name,
            parse: fn
        }
    }


    function parse0(code, index) {
        return {
            pc: index,
            opecode: code[index],
            operand: []
        };
    }

    function parse1(code, index) {
        return {
            pc: index,
            opecode: code[index],
            operand: [code[index + 1]]
        };
    }

    function parse2(code, index) {
        return {
            pc: index,
            opecode: code[index],
            operand: [code[index + 1], code[index + 2]]
        };
    }

    function parse3(code, index) {
        return {
            pc: index,
            opecode: code[index],
            operand: [code[index + 1], code[index + 2], code[index + 3]]
        };
    }

    function parse4(code, index) {
        return {
            pc: index,
            opecode: code[index],
            operand: [code[index + 1], code[index + 2], code[index + 3], code[index + 4]]
        };
    }

    function parseTableswitch(code, index) {
        var defaultByteIndex = index + 1,
            i = index + 1,
            len,
            low,
            high,
            numJumpOffset,
            operand = [];

        while (defaultByteIndex % 4 !== 0) {
            defaultByteIndex++;
        }

        low = code[defaultByteIndex + 4] << 24 | code[defaultByteIndex + 5] << 16 | code[defaultByteIndex + 6] << 8 | code[defaultByteIndex + 7];
        high = code[defaultByteIndex + 8] << 24 | code[defaultByteIndex + 9] << 16 | code[defaultByteIndex + 10] << 8 | code[defaultByteIndex + 11];

        numJumpOffset = (high - low + 1);

        len = defaultByteIndex + (3 + numJumpOffset) * 4 - index + 1;
        while (i < len) {
            operand.push(code[i]);
            i++;
        }
        return {
            pc: index,
            opecode: code[index],
            operand: operand
        };
    }

    function parseLookupswitch(code, index) {
        var defaultByteIndex = index + 1,
            i = index + 1,
            len,
            npairs,
            operand = [];

        while (defaultByteIndex % 4 !== 0) {
            defaultByteIndex++;
        }

        npairs = code[defaultByteIndex + 4] << 24 | code[defaultByteIndex + 5] << 16 | code[defaultByteIndex + 6] << 8 | code[defaultByteIndex + 7];

        len = defaultByteIndex + (2 + npairs * 2) * 4 - index + 1;
        while (i < len) {
            operand.push(code[i]);
            i++;
        }
        return {
            pc: index,
            opecode: code[index],
            operand: operand
        };
    }

    function parseWide(code, index) {
        var operand;

        if (code[index + 1] === 132) {
            operand = [code[index + 1], code[index + 2], code[index + 3]];
        } else {
            operand = [code[index + 1], code[index + 2], code[index + 3], code[index + 4], code[index + 5]];
        }

        return {
            pc: index,
            opecode: code[index],
            operand: operand
        };
    }

    JVM.ByteCodeParser.BYTECODE_DETAIL = BYTECODE_DETAIL;

    JVM.ByteCodeParser.parse = function (code) {
        var i,
            len = code.length,
            byteCode,
            byteCodeArray = [];

        for (i = 0; i < len; i++) {
            try {
                byteCode = BYTECODE_DETAIL[code[i]].parse(code, i);
            } catch (e) {
                throw e;
            }
            byteCodeArray.push(byteCode)

            i += byteCode.operand.length
        }

        return byteCodeArray;

    };


})();



var bin = []; // binary stub;
while(Math.random()) {
    bin.push(Math.random());
}

var loader = new JVM.ClassLoader();
loader.loadClass(bin);
