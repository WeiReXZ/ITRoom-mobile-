// Generated by view binder compiler. Do not edit!
package wi.exest.whattodo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import wi.exest.whattodo.R;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputEditText emailInput;

  @NonNull
  public final TextInputLayout emailLayout;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextInputEditText passwordInput;

  @NonNull
  public final TextInputLayout passwordLayout;

  @NonNull
  public final Button startButton;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextInputEditText emailInput, @NonNull TextInputLayout emailLayout,
      @NonNull ConstraintLayout main, @NonNull TextInputEditText passwordInput,
      @NonNull TextInputLayout passwordLayout, @NonNull Button startButton) {
    this.rootView = rootView;
    this.emailInput = emailInput;
    this.emailLayout = emailLayout;
    this.main = main;
    this.passwordInput = passwordInput;
    this.passwordLayout = passwordLayout;
    this.startButton = startButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emailInput;
      TextInputEditText emailInput = ViewBindings.findChildViewById(rootView, id);
      if (emailInput == null) {
        break missingId;
      }

      id = R.id.emailLayout;
      TextInputLayout emailLayout = ViewBindings.findChildViewById(rootView, id);
      if (emailLayout == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.passwordInput;
      TextInputEditText passwordInput = ViewBindings.findChildViewById(rootView, id);
      if (passwordInput == null) {
        break missingId;
      }

      id = R.id.passwordLayout;
      TextInputLayout passwordLayout = ViewBindings.findChildViewById(rootView, id);
      if (passwordLayout == null) {
        break missingId;
      }

      id = R.id.startButton;
      Button startButton = ViewBindings.findChildViewById(rootView, id);
      if (startButton == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, emailInput, emailLayout, main,
          passwordInput, passwordLayout, startButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
