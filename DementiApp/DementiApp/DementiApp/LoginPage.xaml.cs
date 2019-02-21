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
        private const string Url = "localhost:8080/api/login";
        private readonly HttpClient _client = new HttpClient();
        private ObservableCollection<Post> _posts;

        public LoginPage ()
		{
            InitializeComponent();
            

        }

        private async void Login_Clicked(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MainPage());

        }
    }

    internal class Post : INotifyPropertyChanged
    {
        public int Id { get; set; }

        private string _title;

        [JsonProperty("title")]
        public string Title
        {
            get { return _title; }
            set {
                _title = value;
                OnPropertyChanged();
            }

        }

        public event PropertyChangedEventHandler PropertyChanged;

        protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null) {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

    }
}