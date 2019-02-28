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
		}
        
        async void Button_Clicked(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryEasyPage(userId));
        }

        async void Button_Clicked_1(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryMediumPage(userId));
        }

        async void Button_Clicked_2(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new MemoryPage(userId));
        }
    }
}