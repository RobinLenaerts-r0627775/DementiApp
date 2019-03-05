using Newtonsoft.Json;
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
using Xamarin.Forms.Xaml;

namespace DementiApp
{

    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class StoryPage : ContentPage
    {
        private const string Url = "http://193.191.177.178:8080/api/media/";
        private readonly HttpClient _client = new HttpClient();
        private ObservableCollection<Post> _posts;
        private String userid;
        private String category;

       /*
        * This Code makes sure your JsonData gets converted easily to an object.
        * The object has the properties Data, MediaId, PatientId, Category, File and Description.
        * 
        */
        internal class Post : INotifyPropertyChanged
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

            private string _mediaId;

            [JsonProperty("mediaId")]
            public string MediaId
            {
                get { return _mediaId; }
                set
                {
                    _mediaId = value;
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

            private string _category;

            [JsonProperty("category")]
            public string Category
            {
                get { return _category; }
                set
                {
                    _category = value;
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

            private string _description;

            [JsonProperty("description")]
            public string Description
            {
                get { return _description; }
                set
                {
                    _description = value;
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
         * It requests all pictures of the right category and sets them as the datasource of the ListView. 
         */
        protected override async void OnAppearing()
        {
            if (!CrossConnectivity.Current.IsConnected)
            {
                await DisplayAlert("Oeps", "Kijk na of je verbonden bent met het internet", "Begrepen");
            }
            base.OnAppearing();
            
            try
            {
                string url = Url + userid;
                string content = await _client.GetStringAsync(Url+userid);
                List<Post> posts = JsonConvert.DeserializeObject<List<Post>>(content);
                posts = posts.FindAll(p => p.Category.Equals(category));
                foreach (Post p in posts){
                    Byte[] byteArray = await _client.GetByteArrayAsync("http://193.191.177.178:8080/api/media/data/"+p.MediaId);
                    
                    p.Data  = ImageSource.FromStream(() => new MemoryStream(byteArray));
                    
                }

                _posts = new ObservableCollection<Post>(posts);
                
                MyListView.ItemsSource = _posts;
                showMessage();

            }
            catch (Exception) {
                await DisplayAlert("Oeps", "Kijk na of je verbonden bent met het internet", "Begrepen");
            }

                        
        }

        /*
         * When the page is loading it shows this message to make sure the user understands it. 
         * 
         */
        public async void showMessage() {
            await DisplayAlert("Info", "Klik op een foto om een bijhorende tekst te bekijken", "Ik heb het begrepen");
        }

        public StoryPage(String userId, String cat)
        {
            userid = userId;
            category = cat;
            InitializeComponent();
            NavigationPage.SetHasBackButton(this, false);

        }

        /*
         * When the stack is clicked, the description of the picture is shown. 
         * 
         */
        private void TapGestureRecognizer_Tapped(object sender, EventArgs e)
        {
            var stack = (StackLayout)sender;

            if (stack.Children.Last().IsVisible)
            {
                stack.Children.Last().IsVisible = false;
            }
            else
            {
                stack.Children.Last().IsVisible = true;
            }

            sender = stack;
        }

        private void MyListView_ItemSelected(object sender, SelectedItemChangedEventArgs e)
        {
            ListView listView = (ListView)sender;
            listView.BackgroundColor = Color.LightGray;
            
        }
    }
}