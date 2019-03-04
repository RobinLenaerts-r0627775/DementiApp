using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Net.Http;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class LoginPage : ContentPage
	{

        private const string Url = "http://193.191.177.178:8080/api/login";
        private readonly HttpClient _client = new HttpClient();

        /*
         * This Code makes sure your JsonData gets converted easily to an object.
         * The object has the properties UserId and Password
         * 
        */
        internal class LoginInfo : INotifyPropertyChanged
        {

            private string _userId;

            [JsonProperty("user")]
            public string UserId
            {
                get { return _userId; }
                set
                {
                    _userId = value;
                    OnPropertyChanged();
                }

            }

            private string _password;

            [JsonProperty("password")]
            public string Password
            {
                get { return _password; }
                set
                {
                    _password = value;
                    OnPropertyChanged();
                }

            }
           

            public event PropertyChangedEventHandler PropertyChanged;

            protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
            {
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
            }

        }

        public LoginPage ()
		{
            InitializeComponent();
            NavigationPage.SetHasBackButton(this, false);

        }

        /*
         * This Code makes sure you can't navigate back to your mainpage when you log out. 
         * This function get's called when you press your backbutton on the bottom left of an Android Device.
         */
        protected override bool OnBackButtonPressed()
        {
            return true;
        }

        /*
         * Sends a JsonObject with a Body that contains the properties Username and Password.
         * When the right logininfo is used, the API responds with the right patientId.
         * Shows message when login fails.
         */
        private async void Login_Clicked(object sender, EventArgs e)
        {
            LoginInfo log = new LoginInfo();
            
            if (userId.Text == null || passWd.Text == null)
            {
                Error.IsVisible = true;
                Label l = (Label)Error.Content;
                l.Text = "Vul je naam en wachtwoord in.";
            }
            else
            {
                log.UserId = userId.Text;
                log.Password = passWd.Text;
                try
                {
                    string content = JsonConvert.SerializeObject(log);
                    HttpResponseMessage mess = await _client.PostAsync(Url, new StringContent(content, Encoding.UTF8, "application/json"));
                    var result = await mess.Content.ReadAsStringAsync();
                    if (!result.Equals(""))
                    {
                        String userid = result.Substring(1, result.Length - 2);
                        await Navigation.PushAsync(new MainPage(userid));

                    }
                    else
                    {
                        Error.IsVisible = true;
                        Label l = (Label)Error.Content;
                        l.Text = "Foute Inloggegevens";

                    }
                }
                catch (Exception)
                {
                    Error.IsVisible = true;
                    Label l = (Label)Error.Content;
                    l.Text = "Kan niet verbinden met de server";
                }
            }
            
        }
    }

    
}