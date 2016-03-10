package ksp.kos.ideaplugin.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.lang.ASTFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import ksp.kos.ideaplugin.expressions.Expression;
import ksp.kos.ideaplugin.expressions.SyntaxException;
import ksp.kos.ideaplugin.psi.*;

/**
 * Created on 19/01/16.
 *
 * @author ptasha
 */
public class Differentiate extends BaseAction {
    private static final Logger LOG = Logger.getInstance(Differentiate.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = getEventProject(event);
        if (project == null) return;
        final KerboScriptExpr expr = getDifferentiable(event);
        if (expr == null) return;
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        LOG.debug("Differentiating: " + expr.getText());
        // First, prepare new statement
        final String differentiated;
        try {
            differentiated = diff(expr);
        } catch (ActionFailedException e) {
            final String message = "Cannot differentiate expression. " + e.getMessage();
            LOG.warn(message, e);
            HintManager.getInstance().showErrorHint(editor, message);
            return;
        }
        LOG.debug("Differentiated to: " + differentiated);
        if (StringUtil.isEmptyOrSpaces(differentiated)) {
            final String message = "Differentiation result is empty for " + expr.getText();
            LOG.warn(message);
            HintManager.getInstance().showErrorHint(editor, message);
            return;
        }
        final KerboScriptExpr expression = KerboScriptElementFactory.expression(project, differentiated);
        if (expression == null) {
            final String message = "Cannot construct expression for input '" + differentiated + "'";
            LOG.error(message);
            HintManager.getInstance().showErrorHint(editor, message);
            return;
        }

        // Then update existing model
        WriteCommandAction.runWriteCommandAction(project, () -> {
            KerboScriptInstruction parent = PsiTreeUtil.getParentOfType(expr, KerboScriptInstruction.class, false);
            // Duplicate original statement and rename it if it's not return statement
            if (parent == null) {
                LOG.debug("Unexpected: parent is null for expression ", expr.getText());
            }
            if (parent != null && !(parent instanceof KerboScriptReturnStmt)) {
                PsiElement copy = parent.copy();
                copy.getNode().addChild(ASTFactory.whitespace("\n"));
                parent.getParent().addBefore(copy, parent);
                if (parent instanceof KerboScriptDeclareStmt) {
                    KerboScriptDeclareIdentifierClause identifier = ((KerboScriptDeclareStmt) parent).getDeclareIdentifierClause();
                    if (identifier != null) {
                        identifier.rawRename(identifier.getRawName() + "_");
                    }
                }
            }
            expr.replace(expression);
        });
    }

    private String diff(KerboScriptExpr expr) throws ActionFailedException {
        try {
            return Expression.parse(expr).differentiate().getText();
        } catch (SyntaxException e) {
            throw new ActionFailedException(e);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabled(getDifferentiable(event) != null);
    }

    private KerboScriptExpr getDifferentiable(AnActionEvent event) {
        ExpressionHolder holder = PsiTreeUtil.getParentOfType(getPsiElement(event), ExpressionHolder.class, false);
        if (holder == null) {
            return null;
        }
        return holder.getExpr();
    }
}
