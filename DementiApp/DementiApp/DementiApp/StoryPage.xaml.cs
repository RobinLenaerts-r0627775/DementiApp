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
    public partial class StoryPage : ContentPage
    {
        private const string Url = "localhost:8080/api/media";
        private readonly HttpClient _client = new HttpClient();
        private ObservableCollection<Post> _posts;
        

        internal class Post : INotifyPropertyChanged
        {

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

        protected override async void OnAppearing()
        {
            
            //your code here;
            try
            {
                string content = await _client.GetStringAsync(Url);
                List<Post> posts = JsonConvert.DeserializeObject<List<Post>>(content);
                _posts = new ObservableCollection<Post>(posts);
                MyListView.ItemsSource = _posts;
            }
            catch (Exception e) {
                Console.WriteLine(e.Message);
            }

            showMessage();
            base.OnAppearing();
        }

        public async void showMessage() {
            await DisplayAlert("Info", "Klik op een foto om een bijhorende tekst te bekijken", "Ik heb het begrepen");
        }

        public StoryPage()
        {


            NavigationPage.SetHasBackButton(this, false);

            /*
            //var stack = new StackClickable();
            var list = new ListView();
            
            //LAYOUT
            var scroll = new ScrollView();
            var stacklayout = new StackLayout();
            var title = new Label { Text = "StoryBook", FontSize = 75, HorizontalTextAlignment = TextAlignment.Center };
            var titleFrame = new Frame { Content = title, IsVisible=true, Margin = 10 };
            stacklayout.Children.Add(titleFrame);

            
            for (int i = 0; i < _posts.Count; i++)
            {
                var stack = new StackClickable();
                var img = new Image { Source = "eighty.png" };
                var label = new Label { Text =  _posts[i].Description};
                var frame = new Frame { Content = label};
                stack.Children.Add(img);
                stack.Children.Add(frame);
                stacklayout.Children.Add(stack);
            }
            Content = new ScrollView { Content = stacklayout };
           */
            

        }


    }



    public class StackClickable : StackLayout
    {
        Boolean clicked=false;

        public StackClickable()
        {
            TapGestureRecognizer singleTap = new TapGestureRecognizer()
            {
                NumberOfTapsRequired = 1
            };
            this.GestureRecognizers.Add(singleTap);
            singleTap.Tapped += stack_Clicked;
        }

        private void stack_Clicked(object sender, EventArgs e)
        {

            if (Children.Last().IsVisible)
            {
                Children.Last().IsVisible = false;
            }
            else {
                Children.Last().IsVisible = true;
            }
            
        }
    }
}