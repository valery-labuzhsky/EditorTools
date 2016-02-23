package ksp.kos.ideaplugin.actions;

import com.intellij.lang.ASTFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
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
    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = getEventProject(event);
        if (project == null) return;
        KerboScriptExpr expr = getDifferentiable(event);
        if (expr != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                try {
                    KerboScriptInstruction parent = PsiTreeUtil.getParentOfType(expr, KerboScriptInstruction.class, false);
                    if (parent == null) return;
                    if (!(parent instanceof KerboScriptReturnStmt)) {
                        PsiElement copy = parent.copy();
                        copy.getNode().addChild(ASTFactory.whitespace("\n"));
                        parent.getParent().addBefore(copy, parent);
                        if (parent instanceof KerboScriptDeclareStmt) {
                            KerboScriptDeclareIdentifierClause identifier = ((KerboScriptDeclareStmt) parent).getDeclareIdentifierClause();
                            if (identifier!=null) {
                                identifier.rawRename(identifier.getRawName()+"_");
                            }
                        }
                    }
                    expr.replace(KerboScriptElementFactory.expression(project, diff(expr)));
                } catch (ActionFailedException e) {
                    Messages.showErrorDialog(project, "Exception:" +e.getMessage(), "Differentiate Action Failed");
                }
            });
        }
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
