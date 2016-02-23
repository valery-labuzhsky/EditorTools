package ksp.kos.ideaplugin.format;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import ksp.kos.ideaplugin.psi.KerboScriptTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/01/16.
 *
 * @author ptasha
 */
public class KerboScriptBlock extends AbstractBlock {
    @Nullable
    private Indent myIndent;

    protected KerboScriptBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, @Nullable Indent indent) {
        super(node, wrap, alignment);
        myIndent = indent;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE) {
                Block block = new KerboScriptBlock(child, null, null, indent(child));
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    private Indent indent(ASTNode child) {
        final IElementType myET = myNode.getElementType();
        if (myET instanceof IFileElementType) {
            return Indent.getNoneIndent();
        }
        final IElementType childET = child.getElementType();
        if (myET == KerboScriptTypes.INSTRUCTION_BLOCK) {
            if (childET == KerboScriptTypes.CURLYCLOSE) {
                return Indent.getNoneIndent();
            } else {
                return Indent.getNormalIndent();
            }
        } else if (myET == KerboScriptTypes.DECLARE_PARAMETER_CLAUSE) {
            if (childET == KerboScriptTypes.PARAMETER) {
                return Indent.getNoneIndent();
            } else {
                return Indent.getContinuationIndent();
            }
        } else if (myET == KerboScriptTypes.ARGLIST) {
            return Indent.getContinuationIndent();
        } else if (myET == KerboScriptTypes.FUNCTION_TRAILER) {
            return Indent.getNoneIndent();
        } else if (myET == KerboScriptTypes.SUFFIXTERM) {
            return Indent.getNoneIndent();
        }
        return Indent.getContinuationWithoutFirstIndent();
    }

    @Override
    public Indent getIndent() {
        return myIndent;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return getNode() instanceof LeafElement;
    }

    @Nullable
    @Override
    protected Indent getChildIndent() {
        if (myNode instanceof IFileElementType) {
            return Indent.getNoneIndent();
        }
        final IElementType myET = myNode.getElementType();
        if (myET == KerboScriptTypes.INSTRUCTION_BLOCK) {
            return Indent.getNormalIndent();
        } else if (myET == KerboScriptTypes.ARGLIST) {
            return Indent.getContinuationIndent();
        } else {
            return Indent.getNoneIndent();
        }
    }
}
