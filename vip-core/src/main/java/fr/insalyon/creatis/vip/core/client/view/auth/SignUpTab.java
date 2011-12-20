/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.auth;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.ModulesInit;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

/**
 *
 * @author Rafael Silva
 */
public class SignUpTab extends Tab {

    private ModalWindow modal;
    private DynamicForm form;
    private TextItem firstNameField;
    private TextItem lastNameField;
    private TextItem emailField;
    private TextItem confirmEmailField;
    private TextItem institutionField;
    private TextItem phoneField;
    private PasswordItem passwordField;
    private PasswordItem confirmPasswordField;
    private RadioGroupItem accountRadioGroupItem;
    private TextAreaItem commentItem;
    private CheckboxItem acceptField;
    private IButton signupButton;

    public SignUpTab() {

        this.setID(CoreConstants.TAB_SIGNUP);
        this.setTitle("Sign Up");
        this.setCanClose(true);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureForm();
        configureButton();

        vLayout.addMember(form);
        vLayout.addMember(signupButton);

        this.setPane(vLayout);
    }

    private void configureForm() {

        firstNameField = FieldUtil.getTextItem(300, true, "First Name", null);
        lastNameField = FieldUtil.getTextItem(300, true, "Last Name", null);

        emailField = FieldUtil.getTextItem(300, true, "Your Email", "[a-zA-Z0-9_.\\-+@]");
        emailField.setName("email");
        confirmEmailField = FieldUtil.getTextItem(300, true, "Re-enter Email", "[a-zA-Z0-9_.\\-+@]");

        emailField.setValidators(ValidatorUtil.getEmailValidator());
        confirmEmailField.setValidators(
                ValidatorUtil.getEmailValidator(),
                ValidatorUtil.getMatchesValidator("email", "Emails do not match"));

        institutionField = FieldUtil.getTextItem(300, true, "Institution", null);
        phoneField = FieldUtil.getTextItem(150, true, "Phone", "[0-9\\(\\)\\-+. ]");

        passwordField = new PasswordItem("password", "Password");
        passwordField.setWidth(150);
        passwordField.setLength(32);
        passwordField.setRequired(true);

        confirmPasswordField = new PasswordItem("confPassword", "Re-enter Password");
        confirmPasswordField.setWidth(150);
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setValidators(
                ValidatorUtil.getMatchesValidator("password", "Passwords do not match"));

        accountRadioGroupItem = new RadioGroupItem();
        accountRadioGroupItem.setTitle("Account Type");
        accountRadioGroupItem.setVertical(false);
        accountRadioGroupItem.setValueMap(CoreModule.accountTypes.toArray(new String[]{}));
        accountRadioGroupItem.setRequired(true);

        commentItem = new TextAreaItem("comment", "Comments");
        commentItem.setHeight(80);
        commentItem.setWidth(300);

        TextAreaItem termsField = new TextAreaItem("terms", "Terms of Use");
        termsField.setWidth(300);
        termsField.setHeight(100);
        termsField.setCanFocus(false);
        termsField.setValue(getDisclaimer());

        acceptField = new CheckboxItem("acceptTerms", "I accept the terms of use.");
        acceptField.setRequired(true);
        acceptField.setWidth(150);

        form = FieldUtil.getForm(firstNameField, lastNameField, emailField,
                confirmEmailField, institutionField, phoneField, passwordField,
                confirmPasswordField, accountRadioGroupItem, commentItem,
                termsField, acceptField);
        form.setWidth(500);
        form.setTitleWidth(150);
    }

    private String getDisclaimer() {

        return "-- VIP Terms of Use --\n\n"
                + "This portal is exclusively dedicated to non-commercial academic use. "
                + "It can be accessed free of charge but it is provided \"as is\" without warranty of any kind."
                + "The entire risk as to the quality and performance of the program is with you. VIP can only be used to process and store scientific data using applications registered in the VIP platform. \n\n"
                + "The simulators integrated in the platform must be acknowledged "
                + " as follows:\n\n"
                + "Field II is citationware. If you are publishing any work, "
                + "where this program has been used, please remember that it was "
                + "obtained free of charge. You must reference the two papers "
                + "shown below and the name of the program Field II must be "
                + "mentioned in the publication.\n\n"
                + "[1] J.A.Jensen: Field: A Program for Simulating Ultrasound "
                + "Systems, Paper presented at the 10th Nordic-Baltic Conference "
                + "on Biomedical Imaging Published in Medical & Biological "
                + "Engineering & Computing, pp. 351-353, Volume 34, Supplement 1, "
                + "Part 1, 1996.\n\n"
                + "[2] J.A.Jensen and N.B.Svendsen: Calculation of pressure fields "
                + "from arbitrarily shaped, apodized, and excited ultrasound "
                + "transducers, IEEE Trans.Ultrason., Ferroelec., Freq.Contr., 39, "
                + "pp. 262-267, 1992.\n\n"
                + "Sindbad is developed at CEA-LETI-MINATEC. Access to the simulator has to be specifically requested to Joachim Tabary (joachim.tabary@cea.fr).\n\n"
                + "PET-SORTEO is citationware. If you are publishing any work, where"
                + "this program has been used, please remember that it was obtained free of charge."
                + "You must reference the paper shown below and the name of the program PET-SORTEO must be mentioned in the publication.\n\n"
                + "A. Reilhac, C. Lartizien, N. Costes, S. Sans, C. Comtat, R. Gunn, A. Evans. PET-SORTEO: A Monte Carlo-based simulator with high count rate capabilities. IEEE Transactions on Nuclear Science, 5: 46-52, 2004.\n\n"
                + "If you are publishing any work, where SIMRI has been used, you must reference the paper shown below and the name of the program SIMRI must be mentioned in the publication. You can also mention the SIMRI web site (http://simri.org) and that SIMRI is distributed under the free software CeCiLL license."
                + "\n\nH. Benoit-Cattin, G. Collewet, B. Belaroussi, H. Saint-Jalmes, and C. Odet, \"The SIMRI project: A versatile and interactive MRI simulator\", Journal of Magnetic Resonance, vol. 173, pp. 97-115, 2005.";
    }

    private void configureButton() {

        signupButton = new IButton("Sign Up");
        signupButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {

                    User user = new User(
                            firstNameField.getValueAsString().trim(),
                            lastNameField.getValueAsString().trim(),
                            emailField.getValueAsString().trim(),
                            institutionField.getValueAsString().trim(),
                            passwordField.getValueAsString(),
                            phoneField.getValueAsString().trim());

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to signing up:<br />" + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            modal.hide();
                            SC.say("Your membership request was successfully processed.\n"
                                    + "An activation code was sent to your email.\n"
                                    + "This code will be requested on your first login.");
                            signin();
                        }
                    };
                    modal.show("Signing up...", true);
                    service.signup(user, commentItem.getValueAsString(), callback);
                }
            }
        });
    }

    private void signin() {
        
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                if (caught.getMessage().contains("Authentication failed")) {
                    SC.warn("The username or password you entered is incorrect.");
                } else {
                    SC.warn("Unable to signing in:\n" + caught.getMessage());
                }
            }

            public void onSuccess(User result) {
                modal.hide();
                ModulesInit.getInstance().parseAccountType(accountRadioGroupItem.getValueAsString());
                Layout.getInstance().removeTab(CoreConstants.TAB_SIGNIN);
                Layout.getInstance().removeTab(CoreConstants.TAB_SIGNUP);

                Cookies.setCookie(CoreConstants.COOKIES_USER,
                        result.getEmail(), CoreConstants.COOKIES_EXPIRATION_DATE,
                        null, "/", false);
                Cookies.setCookie(CoreConstants.COOKIES_SESSION,
                        result.getSession(), CoreConstants.COOKIES_EXPIRATION_DATE,
                        null, "/", false);

                Layout.getInstance().authenticate(result);
            }
        };
        modal.show("Signing in...", true);
        service.signin(emailField.getValueAsString().trim(),
                passwordField.getValueAsString(), callback);
    }
}
