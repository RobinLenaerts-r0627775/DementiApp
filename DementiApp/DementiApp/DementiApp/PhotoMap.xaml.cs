using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class PhotoMap : ContentPage
    {
        private List<String> titlesArrayList = new List<String>();
        private const string Url = "http://193.191.177.178:8080/category/";
        private readonly HttpClient _client = new HttpClient();
        private ObservableCollection<String> _categories;
        private String userid;

        protected async override void OnAppearing()
        {
            string content = await _client.GetStringAsync(Url + "/" + userid);
            List<String> categories = JsonConvert.DeserializeObject<List<String>>(content);
            _categories = new ObservableCollection<String>(categories);

            Categories.ItemsSource = _categories;

        }

        public PhotoMap(String userId)
        {
            userid = userId;
            InitializeComponent();


            var labelStyle = new Style(typeof(Label))
            {
                Setters = {
                    new Setter { Property = Label.BackgroundColorProperty, Value = Color.FromHex("#372c73")},
                    new Setter { Property = Label.TextColorProperty, Value = Color.White},
                    new Setter { Property = Label.FontSizeProperty, Value=35 },
                    new Setter { Property = Label.HorizontalTextAlignmentProperty, Value=TextAlignment.Center},
                    new Setter {Property = Label.WidthRequestProperty, Value=500},
                    new Setter {Property = Label.HeightRequestProperty, Value=100}
                }
            };

            var frameStyle = new Style(typeof(Frame))
            {
                Setters = {
                    new Setter { Property =Frame.CornerRadiusProperty, Value = 25 },
                    new Setter { Property =Frame.PaddingProperty, Value = 0 },
                    new Setter { Property =Frame.MarginProperty, Value= new Thickness (10,10,10,10)},
                    new Setter { Property = Frame.IsVisibleProperty, Value=true},
                    new Setter { Property = Frame.HasShadowProperty, Value=true},
                    new Setter { Property = Frame.HeightRequestProperty, Value=100}

                }
            };

            var gridStyle = new Style(typeof(RowDefinition))
            {
                Setters = {
                    new Setter { Property = RowDefinition.HeightProperty, Value=100 }
                }
            };


            Resources = new ResourceDictionary();
            Resources.Add(labelStyle);
            Resources.Add(frameStyle);
            Resources.Add(gridStyle);

            titlesArrayList = new List<String>();


        }
    }
}