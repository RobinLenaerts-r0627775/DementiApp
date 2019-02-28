using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace DementiApp
{
    public partial class MainPage : ContentPage
    {
        private const string Url = "http://193.191.177.178:8080/api/patients";
        private readonly HttpClient _client = new HttpClient();
        private String userid;

        internal class Patient : INotifyPropertyChanged
        {


            private ImageSource _data;

            public ImageSource Data
            {
                get { return _data; }
                set
                {
                    _data = value;
                    OnPropertyChanged();

                }

            }

            private string _birthdDate;

            [JsonProperty("birthdDate")]
            public string BirthdDate
            {
                get { return _birthdDate; }
                set
                {
                    _birthdDate = value;
                    OnPropertyChanged();
                }

            }

            private string _dementiaLevel;

            [JsonProperty("dementiaLevel")]
            public string DementiaLevel
            {
                get { return _dementiaLevel; }
                set
                {
                    _dementiaLevel = value;
                    OnPropertyChanged();
                }

            }

            private string _firstName;

            [JsonProperty("firstName")]
            public string FirstName
            {
                get { return _firstName; }
                set
                {
                    _firstName = value;
                    OnPropertyChanged();
                }

            }

            private string _lastName;

            [JsonProperty("lastName")]
            public string LastName
            {
                get { return _lastName; }
                set
                {
                    _lastName = value;
                    OnPropertyChanged();
                }

            }

            private string _patientId;

            [JsonProperty("patientId")]
            public string PatientId
            {
                get { return _patientId; }
                set
                {
                    _patientId = value;
                    OnPropertyChanged();
                }

            }

            private string _file;

            [JsonProperty("file")]
            public string File
            {
                get { return _file; }
                set
                {
                    _file = value;
                    OnPropertyChanged();
                }

            }

            private string _profilePicture;

            [JsonProperty("profilePicture")]
            public string ProfilePicture
            {
                get { return _profilePicture; }
                set
                {
                    _profilePicture = value;
                    OnPropertyChanged();
                }

            }

            private string _role;

            [JsonProperty("role")]
            public string Role
            {
                get { return _role; }
                set
                {
                    _role = value;
                    OnPropertyChanged();
                }

            }

            public event PropertyChangedEventHandler PropertyChanged;

            protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
            {
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
            }

        }

        protected override async void OnAppearing() {
            string t = Url+"/"+userid;
            string content = await _client.GetStringAsync(Url+"/"+userid);
            Patient patient = JsonConvert.DeserializeObject<Patient>(content);

            Byte[] byteArray=null;
            try
            {
                byteArray = await _client.GetByteArrayAsync("http://193.191.177.178:8080/api/media/data/" + patient.ProfilePicture);
            }
            catch (Exception e) {
                await DisplayAlert("Error", e.Message, "Cancel");
            }

                patient.Data = ImageSource.FromStream(() => new MemoryStream(byteArray));
                profilePhoto.Source = patient.Data;
                naam.Text = patient.FirstName + " " + patient.LastName;
                       
            
        }

        public MainPage(String userId)
        {
            this.userid = userId;
            InitializeComponent();
            NavigationPage.SetHasBackButton(this, false);


        }

        async void ToMemory(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryDiffPage(userid));
        }
        /*async void ToStory(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new StoryPage(userid));
        }*/
        async void ToMusicQuiz(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MusicQuizPage());
        }

        async void ToPhotoMap(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new PhotoMap(userid));
        }

        protected override bool OnBackButtonPressed()
        {
            Device.BeginInvokeOnMainThread(async () => {
                var result = await this.DisplayAlert("Pas op!", "Wil je zeker uitloggen?", "Ja", "Nee");
                if (result) await this.Navigation.PushAsync(new LoginPage()); // or anything else
            });

            return true;


        }

        async void showAlert() {
            await DisplayAlert("Pas op!", "Ben je zeker dat je wilt uitloggen?", "Ja", "Nee");
        }
    }
}
