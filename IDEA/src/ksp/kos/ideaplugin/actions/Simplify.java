package ksp.kos.ideaplugin.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import ksp.kos.ideaplugin.expressions.Expression;
import ksp.kos.ideaplugin.expressions.SyntaxException;
import ksp.kos.ideaplugin.psi.ExpressionHolder;
import ksp.kos.ideaplugin.psi.KerboScriptElementFactory;
import ksp.kos.ideaplugin.psi.KerboScriptExpr;

/**
 * Created on 24/01/16.
 *
 * @author ptasha
 */
public class Simplify extends BaseAction {
    private static final Logger LOG = Logger.getInstance(Simplify.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = getEventProject(event);
        if (project == null) return;
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        final KerboScriptExpr expr = getSimplifiable(event);
        if (expr == null) return;
        final KerboScriptExpr simplify;
        try {
            simplify = simplify(expr);
        } catch (ActionFailedException e) {
            final String message = "Cannot simplify expression '" + expr + "': " + e.getMessage();
            LOG.warn(message, e);
            HintManager.getInstance().showErrorHint(editor, message);
            return;
        }
        WriteCommandAction.runWriteCommandAction(project, () -> {
            expr.replace(simplify);
        });
    }

    private KerboScriptExpr simplify(KerboScriptExpr expr) throws ActionFailedException {
        try {
            Expression parse = Expression.parse(expr);
            String simple = parse.simplify().getText();
            return KerboScriptElementFactory.expression(expr.getProject(), simple);
        } catch (SyntaxException e) {
            throw new ActionFailedException(e);
        }
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabled(getSimplifiable(event) != null);
    }

    private KerboScriptExpr getSimplifiable(AnActionEvent event) {
        ExpressionHolder holder = PsiTreeUtil.getParentOfType(getPsiElement(event), ExpressionHolder.class, false);
        if (holder == null) {
            return null;
        }
        return holder.getExpr();
    }
}
