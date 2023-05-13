package p1.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class SuperPartitionTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "SuperPartitionTransformer";
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {

        if (reader.getClassName().equals("p1/sort/HybridSortRandomPivot")) {
            reader.accept(new CV(Opcodes.ASM9, writer), 0);
        } else {
            reader.accept(writer, 0);
        }

    }

    private static class CV extends ClassVisitor {

        public CV(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MV(api, super.visitMethod(access, name, descriptor, signature, exceptions));
        }
    }

    private static class MV extends MethodVisitor {

        public MV(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (owner.equals("p1/sort/HybridSort") && name.equals("partition")) {
                visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "p1/HybridSortRandomPivotTests",
                    "partition",
                    "(Lp1/sort/SortList;II)I",
                    false
                );
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        }
    }
}
