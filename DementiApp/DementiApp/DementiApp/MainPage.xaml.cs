using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace DementiApp
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            InitializeComponent();
            NavigationPage.SetHasBackButton(this, false);


        }

        async void ToMemory(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryPage());
        }
        async void ToStory(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new StoryPage());
        }
        async void ToMusicQuiz(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MusicQuizPage());
        }

        async void ToPhotoMap(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new PhotoMap());
        }
    }
}
