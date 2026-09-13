import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { MarketData } from '../model/market-data';
import { MarketDataService } from '../service/market-data.service';

@Component({
  selector: 'app-market-data',
  imports: [FormsModule],
  templateUrl: './market-data.html',
  styleUrl: './market-data.css'
})
export class MarketDataComponent {

  // Création / modification
  symbole = '';
  date = '';
  prix = 0;
  volume = 0;

  listeMarketData: MarketData[] = [];

  // Lecture
  marketData?: MarketData;
  idRecherche = 0;

  // Modification
  idModification = 0;

  // Suppression
  idSuppression = 0;

  // Fichier
  selectedFile?: File;

  // Etat de l'interface
  loading = false;
  message = '';
  error = '';

  constructor(
    private service: MarketDataService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ajouter() {
    const marketData: MarketData = {
      symbole: this.symbole,
      date: this.date,
      prix: this.prix,
      volume: this.volume
    };

    this.listeMarketData.push(marketData);

    this.message = 'MarketData ajouté à la liste';
    this.error = '';
  }

  envoyer() {
    if (this.listeMarketData.length === 0) {
      this.error = 'Aucun MarketData à envoyer';
      this.message = '';
      return;
    }

    this.loading = true;
    this.message = '';
    this.error = '';

    this.service.insert(this.listeMarketData).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'MarketData envoyés avec succès';
        this.listeMarketData = [];

        this.cdr.detectChanges();
      },

      error: () => {
        this.loading = false;
        this.error = 'Erreur lors de l\'envoi des MarketData';

        this.cdr.detectChanges();
      }
    });
  }

  rechercher() {
    this.loading = true;
    this.message = '';
    this.error = '';

    this.service.getById(this.idRecherche).subscribe({
      next: data => {
        this.marketData = data;
        this.loading = false;
        this.message = 'MarketData trouvé';

        this.cdr.detectChanges();
      },

      error: () => {
        this.marketData = undefined;
        this.loading = false;
        this.error = 'Impossible de récupérer ce MarketData';

        this.cdr.detectChanges();
      }
    });
  }

  modifier() {
    const marketData: MarketData = {
      symbole: this.symbole,
      date: this.date,
      prix: this.prix,
      volume: this.volume
    };

    this.loading = true;
    this.message = '';
    this.error = '';

    this.service.update(this.idModification, marketData).subscribe({
      next: data => {
        this.marketData = data;
        this.loading = false;
        this.message = 'MarketData modifié avec succès';

        this.cdr.detectChanges();
      },

      error: () => {
        this.loading = false;
        this.error = 'Erreur lors de la modification';

        this.cdr.detectChanges();
      }
    });
  }

  supprimer() {
    this.loading = true;
    this.message = '';
    this.error = '';

    this.service.delete(this.idSuppression).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'MarketData supprimé avec succès';
        this.marketData = undefined;

        this.cdr.detectChanges();
      },

      error: () => {
        this.loading = false;
        this.error = 'Erreur lors de la suppression';

        this.cdr.detectChanges();
      }
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.message = 'Fichier sélectionné : ' + this.selectedFile.name;
      this.error = '';
    }
  }

  envoyerFichier() {
    if (!this.selectedFile) {
      this.error = 'Aucun fichier sélectionné';
      this.message = '';
      return;
    }

    this.loading = true;
    this.message = '';
    this.error = '';

    this.service.uploadFile(this.selectedFile).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Fichier envoyé avec succès';
        this.selectedFile = undefined;

        this.cdr.detectChanges();
      },

      error: () => {
        this.loading = false;
        this.error = 'Erreur lors de l\'envoi du fichier';

        this.cdr.detectChanges();
      }
    });
  }
}
