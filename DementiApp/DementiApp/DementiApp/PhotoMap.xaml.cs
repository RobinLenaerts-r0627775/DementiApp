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
        private const string Url = "http://193.191.177.178:8080/api/patients/category/";
        private readonly HttpClient _client = new HttpClient();
        private ObservableCollection<String> _categories;
        private String userid;

        protected async override void OnAppearing()
        {
            string content = await _client.GetStringAsync(Url + userid);
            List<String> categories = JsonConvert.DeserializeObject<List<String>>(content);
            _categories = new ObservableCollection<String>(categories);

            Categories.ItemsSource = _categories;

        }

        public PhotoMap(String userId)
        {
            userid = userId;
            InitializeComponent();
            NavigationPage.SetHasBackButton(this, false);




        }

        private void TapGestureRecognizer_Tapped(object sender, EventArgs e)
        {
            StackLayout stack= (StackLayout)sender;
            StackLayout stack2 = (StackLayout) stack.Children[0];
            Frame frame = (Frame)stack2.Children[0];
            Label l = (Label)frame.Content;
            
            Navigation.PushAsync(new StoryPage(userid, l.Text));
        }
    }
}