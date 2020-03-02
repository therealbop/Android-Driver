package com.karry.dagger;



import com.karry.app.adv.AdvertiseActivity;
import com.karry.app.adv.AdvertiseModule;
import com.karry.app.adv.AdvertiseUtilModule;
import com.karry.app.cancelBooking.CancelReasonUtilModule;
import com.karry.app.splash.SplashScreenActivity;
import com.karry.app.bookingRequest.BookingPopUpActivity;
import com.karry.app.bookingRequest.BookingPopupModule;
import com.karry.app.cancelBooking.CancelReasonActivity;
import com.karry.app.cancelBooking.CancelReasonModule;
import com.karry.app.meterBooking.DriverMeterActivity;
import com.karry.app.meterBooking.DriverMeterDaggerModule;
import com.karry.app.mainActivity.MainActivity;
import com.karry.app.mainActivity.MainActiviyDaggerModule;
import com.karry.app.meterBooking.DriverMeterUtilModule;
import com.karry.app.meterBooking.address.AddressSelectModule;
import com.karry.app.meterBooking.address.PlaceAutoCompleteAdapterModule;
import com.karry.app.meterBooking.address.AddressSelectionActivity;
import com.karry.app.splash.SplashScreenDaggerModule;
import com.karry.app.splash.SplashScreenUtilModule;
import com.karry.authentication.login.LoginUtilModule;
import com.karry.authentication.vehicleTypeList.VehicleTypeListActivity;
import com.karry.authentication.vehicleTypeList.VehicleTypeListAdapterModule;
import com.karry.authentication.vehicleTypeList.VehicleTypeListModule;
import com.karry.mqttChat.ChattingActivity;
import com.karry.mqttChat.ChattingModule;
import com.karry.side_screens.bankDetails.bankAccountAdd.BankNewAccountActivity;
import com.karry.side_screens.bankDetails.bankAccountAdd.BankNewAccountModule;
import com.karry.side_screens.bankDetails.bankStripeAccountAdd.BankStripeAddActivity;
import com.karry.side_screens.bankDetails.bankStripeAccountAdd.BankStripeAddModule;
import com.karry.authentication.forgotpassword.ChangePasswordActivity;
import com.karry.authentication.forgotpassword.ChangePasswordModule;
import com.karry.authentication.forgotpassword.ForgotPasswordActivity;
import com.karry.authentication.forgotpassword.ForgotPasswordModule;
import com.karry.authentication.forgotpassword.OTPVerificationActivity;
import com.karry.authentication.forgotpassword.OTPVerificationModule;
import com.karry.side_screens.help_center.zendeskHelpIndex.ZendeskAdapterModule;
import com.karry.side_screens.help_center.zendeskHelpIndex.ZendeskHelpIndex;
import com.karry.side_screens.help_center.zendeskHelpIndex.ZendeskModule;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpIndexAdapterModule;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpIndexTicketDetails;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpTicketDetailsModule;
import com.karry.side_screens.history.history_invoice.HelpAdapterModule;
import com.karry.side_screens.history.history_invoice.HistoryInvoiceActivity;
import com.karry.side_screens.history.history_invoice.HistoryInvoiceActivityDaggerModule;
import com.karry.side_screens.home_fragment.invoice.InvoiceActivity;
import com.karry.side_screens.home_fragment.invoice.InvoiceDaggerModule;
import com.karry.side_screens.home_fragment.invoice.InvoiceDialogModule;
import com.karry.bookingFlow.RideBookingActivity;
import com.karry.bookingFlow.RideBookingDaggerModule;
import com.karry.bookingFlow.RideBookingUtilityModule;
import com.karry.authentication.login.LoginActivity;
import com.karry.authentication.login.LoginModule;
import com.karry.side_screens.portal.PortalActivity;
import com.karry.side_screens.portal.PortalDaggerModule;
import com.karry.side_screens.prefered_zone.PreferedZoneListActivity;
import com.karry.side_screens.prefered_zone.PreferedZoneListDaggerModule;
import com.karry.side_screens.prefered_zone.PreferedZoneModule;
import com.karry.side_screens.prefered_zone.preferedZoneSelect.PreferedZoneSelectActivity;
import com.karry.side_screens.prefered_zone.preferedZoneSelect.PreferedZoneSelectDaggerModule;
import com.karry.side_screens.profile.EditProfileActivity;
import com.karry.side_screens.profile.EditProfileModule;
import com.karry.service.LocationUpdateService;
import com.karry.authentication.signup.SignUpDocument.ImageAdapterModule;
import com.karry.authentication.signup.SignUpDocument.SignUpDocumentActivity;
import com.karry.authentication.signup.SignUpDocument.SignUpDocumentModule;
import com.karry.authentication.signup.SignUpPersonal.SignupPersonalActvity;
import com.karry.authentication.signup.SignUpPersonal.SignupPersonalDaggerModule;
import com.karry.authentication.signup.SignUpPersonal.SigupUtilsModule;
import com.karry.authentication.signup.SignUpVehicle.SignUpVehUtilsModule;
import com.karry.authentication.signup.SignUpVehicle.SignupVehicleActivity;
import com.karry.authentication.signup.SignUpVehicle.SignupVehicleModule;
import com.karry.authentication.signup.SignUpWeb.RegisterWebActivity;
import com.karry.authentication.signup.SignUpWeb.RegisterWebModule;
import com.karry.side_screens.support.supportSubCategory.SupportSubCatDaggerModule;
import com.karry.side_screens.support.supportSubCategory.SupportSubCategoryActivity;
import com.karry.authentication.vehiclelist.AdapterModule;
import com.karry.authentication.vehiclelist.VehicleListActivity;
import com.karry.authentication.vehiclelist.VehicleListModule;
import com.karry.side_screens.wallet.add_card.AddCardActivity;
import com.karry.side_screens.wallet.add_card.AddCardActivityModule;
import com.karry.side_screens.wallet.changeCard.ChangeCardActivity;
import com.karry.side_screens.wallet.changeCard.ChangeCardDaggerModule;
import com.karry.side_screens.wallet.changeCard.ChangeCardUtilModule;
import com.karry.side_screens.wallet.wallet_transaction_activity.WalletTransActivity;
import com.karry.side_screens.wallet.wallet_transaction_activity.WalletTransactionActivityDaggerModule;
import com.karry.telecall.audiocall.AudioCallModule;
import com.karry.telecall.callservice.AudioCallService;
import com.karry.telecall.incommingcalls.IncomingCallDaggerModule;
import com.karry.telecall.incommingcalls.IncomingCallScreen;
import com.karry.telecall.utility.OnMyService;
import com.karry.twilio_call.ClientActivity;
import com.karry.twilio_call.ClientActivityDaggerModule;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {


    @ContributesAndroidInjector
    abstract LocationUpdateService locatioUpdateService();

   /* @ContributesAndroidInjector
    abstract NetworkCheckerService provideNetworkCheckerService();*/


    /**********************************************************************************************/
    @ActivityScoped
    @ContributesAndroidInjector(modules = {SplashScreenDaggerModule.class, SplashScreenUtilModule.class})
    abstract SplashScreenActivity splashScreenActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {LoginModule.class, LoginUtilModule.class})
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = ForgotPasswordModule.class)
    abstract ForgotPasswordActivity forgotPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = OTPVerificationModule.class)
    abstract OTPVerificationActivity otpVerificationActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = ChangePasswordModule.class)
    abstract ChangePasswordActivity changePasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {VehicleListModule.class,AdapterModule.class})
    abstract VehicleListActivity vehicleList();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {VehicleTypeListModule.class,VehicleTypeListAdapterModule.class})
    abstract VehicleTypeListActivity vehicleTypeListActivity ();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {RegisterWebModule.class})
    abstract RegisterWebActivity registerWebActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {SignupPersonalDaggerModule.class, SigupUtilsModule.class})
    abstract SignupPersonalActvity signupPersonalActvity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {SignupVehicleModule.class, SignUpVehUtilsModule.class})
    abstract SignupVehicleActivity signupVehicleActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {SignUpDocumentModule.class, ImageAdapterModule.class})
    abstract SignUpDocumentActivity signUpDocumentActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {MainActiviyDaggerModule.class})
    public abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={BookingPopupModule.class})
    abstract BookingPopUpActivity bookingPopUp();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {EditProfileModule.class})
    abstract EditProfileActivity editPhoneEmailActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {SupportSubCatDaggerModule.class})
    public abstract SupportSubCategoryActivity supportSubCategoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {BankStripeAddModule.class})
    public abstract BankStripeAddActivity bankStripeAddActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {BankNewAccountModule.class})
    public abstract BankNewAccountActivity bankNewAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RideBookingDaggerModule.class, RideBookingUtilityModule.class})
    abstract RideBookingActivity landingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletTransactionActivityDaggerModule.class})
    abstract WalletTransActivity walletTransactionsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChangeCardDaggerModule.class, ChangeCardUtilModule.class})
    abstract ChangeCardActivity changeCardActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddCardActivityModule.class})
    abstract AddCardActivity addCardActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {DriverMeterDaggerModule.class, DriverMeterUtilModule.class})
    abstract DriverMeterActivity driverMeterActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CancelReasonModule.class, AdapterModule.class, CancelReasonUtilModule.class})
    abstract CancelReasonActivity cancelReasonActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {InvoiceDaggerModule.class, InvoiceDialogModule.class})
    abstract InvoiceActivity invoiceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddressSelectModule.class, PlaceAutoCompleteAdapterModule.class})
    abstract AddressSelectionActivity addressSelectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HistoryInvoiceActivityDaggerModule.class,  HelpAdapterModule.class})
    abstract HistoryInvoiceActivity historyInvoiceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PortalDaggerModule.class})
    abstract PortalActivity portalActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PreferedZoneListDaggerModule.class, PreferedZoneModule.class})
    abstract PreferedZoneListActivity preferedZoneListActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PreferedZoneSelectDaggerModule.class})
    abstract PreferedZoneSelectActivity preferedZoneSelectActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChattingModule.class})
    abstract ChattingActivity chattingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ClientActivityDaggerModule.class})
    abstract ClientActivity clientActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AdvertiseModule.class, AdvertiseUtilModule.class})
    abstract AdvertiseActivity advertiseDialog();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpTicketDetailsModule.class, HelpIndexAdapterModule.class})
    abstract HelpIndexTicketDetails provideHelpIndexDetails();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ZendeskModule.class, ZendeskAdapterModule.class})
    abstract ZendeskHelpIndex provideZendeskHelp();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AudioCallModule.class})
    abstract AudioCallService provideAudioCallService();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {IncomingCallDaggerModule.class})
    abstract IncomingCallScreen IncomingCallActivity();


    @ContributesAndroidInjector
    abstract OnMyService service();


}
