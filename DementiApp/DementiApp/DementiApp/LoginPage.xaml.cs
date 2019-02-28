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
            

        }

        private async void Login_Clicked(object sender, EventArgs e)
        {

            LoginInfo log = new LoginInfo();
            log.UserId = userId.Text;
            log.Password = passWd.Text;

            try
            {
                string content = JsonConvert.SerializeObject(log);
                HttpResponseMessage mess = await _client.PostAsync(Url, new StringContent(content, Encoding.UTF8, "application/json"));
                var result = await mess.Content.ReadAsStringAsync();
                if (result != null)
                {
                    await Navigation.PushAsync(new MainPage(result));

                }
                else
                {
                    Error.IsVisible = true;
                    Label l = (Label)Error.Content;
                    l.Text = "Deze inloggegevens kloppen niet, probeer opnieuw";
                    //Gewoon Voor Testen
                    
                }
            }
            catch (TimeoutException)
            {
                Error.IsVisible = true;
                Label l = (Label)Error.Content;
                l.Text = "Fout met de Server";
            }
            catch (Exception) {
                Error.IsVisible = true;
                Label l = (Label)Error.Content;
                l.Text = "Fout met de Server";
            }
            
        }
    }

    
}