using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class WinPage : ContentPage
    {
        private String userId;
        public WinPage(String userid)
        {
            userId = userid;
            InitializeComponent();
        }

        async void Button_Clicked(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryPage(userId));
        }
        protected override bool OnBackButtonPressed()
        {
            Navigation.PushAsync(new MainPage(userId));
            return true;
        }
    }
}