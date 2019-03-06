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
	public partial class MemoryDiffPage : ContentPage
	{

        private String userId;

		public MemoryDiffPage (String userid)
		{
            userId = userid;
			InitializeComponent ();
            NavigationPage.SetHasBackButton(this, false);
        }

        /*
         *Wanneer je op de terugknop klikt, ga je automatisch naar het mainscherm 
         * 
         */
        protected override bool OnBackButtonPressed()
        {
            Device.BeginInvokeOnMainThread(async () => {
                await this.Navigation.PushAsync(new MainPage(userId)); // or anything else
            });

            return true;
        }

        /*
         * Wanneer je op deze knop druk start je een makkelijk memoryspel.
         */ 
        async void Button_Clicked(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryEasyPage(userId));
        }

        /*
         * Wanneer je op deze knop druk start je een gemiddeld memoryspel.
         */
        async void Button_Clicked_1(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryMediumPage(userId));
        }

        /*
         * Wanneer je op deze knop druk start je een moeilijk memoryspel.
         */
        async void Button_Clicked_2(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryPage(userId));
        }
    }
}