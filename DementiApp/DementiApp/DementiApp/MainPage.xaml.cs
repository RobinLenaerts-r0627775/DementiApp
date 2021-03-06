﻿using Newtonsoft.Json;
using Plugin.Connectivity;
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


        /*
        * This Code makes sure your JsonData gets converted easily to an object.
        * The object has the properties Data, FirstName, LastName, PatientId, File, ProfilePicture and Role.
        * 
        */
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


        /*
         * The OnAppearing function gets called when the constructor calls the InitialiseComponent() function.
         * It requests the id of your profilepicture and then requests your profilepicture. 
        */
        protected override async void OnAppearing() {
            try
            {
                string t = Url+"/"+userid;
                string content = await _client.GetStringAsync(Url+"/"+userid);
                Patient patient = JsonConvert.DeserializeObject<Patient>(content);

                Byte[] byteArray=null;
            
                byteArray = await _client.GetByteArrayAsync("http://193.191.177.178:8080/api/media/data/" + patient.ProfilePicture);

                patient.Data = ImageSource.FromStream(() => new MemoryStream(byteArray));
                profilePhoto.Source = patient.Data;
                naam.Text = patient.FirstName + " " + patient.LastName;
            }
            catch (Exception)
            {
                await DisplayAlert("Oeps", "Kijk na of je verbonden bent met het internet", "Begrepen");
            }

            
                       
            
        }

        public MainPage(String userId)
        {
            this.userid = userId;
            InitializeComponent();
            this.Title = DateTime.Today.ToString("dd/MM/yyyy");
            NavigationPage.SetHasBackButton(this, false);

        }

        /*
         * Navigates to the MemoryGame. 
         */
        async void ToMemory(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryDiffPage(userid));
        }

        /*
         * Navigates to the page with all the categories. 
         */
        async void ToPhotoMap(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new PhotoMap(userid));
        }

        /*
         * Navigates to the Login Page
         * Displays an alert to make sure you want to log out.
         */
        protected override bool OnBackButtonPressed()
        {
            Device.BeginInvokeOnMainThread(async () => {
                var result = await this.DisplayAlert("Pas op!", "Wil je zeker uitloggen?", "Ja", "Nee");
                if (result) await this.Navigation.PushAsync(new LoginPage()); // or anything else
            });

            return true;


        }
    }
}
